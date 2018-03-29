package com.yaya.sdk.tcp.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yaya.sdk.MLog;
import com.yaya.sdk.MessageFilter;
import com.yaya.sdk.tcp.ITcp;
import com.yaya.sdk.tcp.ITcpConnection;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.TcpExceptionHandler;
import com.yaya.sdk.tcp.TcpResponseCallback;
import com.yaya.sdk.tcp.TcpTimeoutCallback;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/9.
 */
public class YayaTcp implements ITcp {

    private static final String TAG = "YayaTcp";

    private static YayaTcp instance;

    public static ITcp getInstance() {
        if(instance == null) {
            instance = new YayaTcp();
        }
        return instance;
    }

    private TcpConnection mTcpConnection;
    private TcpExceptionHandler mTcpExceptionHandler;

    private YayaThreadManager mYayaThreadManager;
    private LocalHandler mHandler;

    //Key is request's seqNum
    private final ConcurrentHashMap<Long, TcpRequest> mInflightRequests = new ConcurrentHashMap<>();

    private Map<Class, ResponseDispatcher> mCustomDispatchers = new HashMap<>();
    private final Object dispatcherListLock = new Object();

    private final List<TcpExceptionHandler> mTcpExceptionHandlerList = new ArrayList<>();
    private final Object exceptionHandlerListLock = new Object();

    private MessageFilter mMessageFilter;

    private YayaTcp() {}

    @Override
    public void open() {
        synchronized (this) {
            if(mYayaThreadManager == null || !mYayaThreadManager.isAlive()) {
                mYayaThreadManager = new YayaThreadManager();
                mYayaThreadManager.start();
                mHandler = new LocalHandler(mYayaThreadManager.getYayaLooper());
            }

            if(mTcpConnection == null) {
                mTcpExceptionHandler = getTcpExceptionHandler();
                mTcpConnection = new TcpConnection(getResponseDispatcher(),
                        mTcpExceptionHandler);
            }

            if(mHandler == null) {
                mHandler = new LocalHandler(mYayaThreadManager.getYayaLooper());
            }
        }
    }

    @Override
    public void setMessageFilter(MessageFilter filter) {
        synchronized (dispatcherListLock) {
            mMessageFilter = filter;
        }
    }

    @Override
    public void close() {
        synchronized (dispatcherListLock) {
            mCustomDispatchers.clear();
            mMessageFilter = null;
        }
        synchronized (exceptionHandlerListLock) {
            mTcpExceptionHandlerList.clear();
        }
        synchronized (this) {
            if(mYayaThreadManager != null) {
                mYayaThreadManager.stop();
            }
            if(mTcpConnection != null) {
                mTcpConnection.disconnect();
            }
            mInflightRequests.clear();
            mYayaThreadManager = null;
            mTcpConnection = null;
            mHandler = null;
        }
        YayaTcp.instance = null;
    }

    @Override
    public Looper getYayaThreadLooper() {
        if(mYayaThreadManager == null) {
            return null;
        }
        return mYayaThreadManager.getYayaLooper();
    }

    @Override
    public ITcpConnection getConnection() {
        return mTcpConnection;
    }

    @Override
    public long sendAndListen(final TcpRequest request) {
        if(!mTcpConnection.isConnected()) {
            return -1;
        }
        long seq = request.getSeqNum();
        if(mInflightRequests.containsKey(seq)) {
            return -2;
        }
        mInflightRequests.put(seq, request);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MLog.d(TAG, "write " + request.getTlvSignal().toString());
                if(!mTcpConnection.isConnected()) {
                    MLog.e(TAG, "connection is closed!");
                    TcpResponseCallback callback = request.getTcpResponseCallback();
                    if(callback != null) {
                        callback.onTcpResponse(request.getTlvSignal(), null);
                    }
                    return;
                }
                mTcpConnection.write(request.getTlvSignal());
            }
        });
        return seq;
    }

    @Override
    public long sendAndListen(TcpRequest request, long timeout) {
        mHandler.recordTimeout(request.getSeqNum(), timeout);
        return sendAndListen(request);
    }

    @Override
    public void cancelRequest(long seqNum) {
        synchronized (this) {
            if (mInflightRequests.containsKey(seqNum)) {
                mInflightRequests.remove(seqNum);
            }
        }
    }

    @Override
    public void cancelAllRequest() {
        synchronized (this) {
            mInflightRequests.clear();
        }
    }

    @Override
    public void registerExceptionHandler(TcpExceptionHandler handler) {
        synchronized (exceptionHandlerListLock) {
            if(!mTcpExceptionHandlerList.contains(handler)) {
                mTcpExceptionHandlerList.add(handler);
            }
        }
    }

    @Override
    public void unregisterExceptionHandler(TcpExceptionHandler handler) {
        synchronized (exceptionHandlerListLock) {
            mTcpExceptionHandlerList.remove(handler);
        }
    }

    @Override
    public void registerSignalDispatcher(Class clazz, ResponseDispatcher dispatcher) {
        synchronized (dispatcherListLock) {
            if(!mCustomDispatchers.containsKey(clazz)) {
                mCustomDispatchers.put(clazz, dispatcher);
            } else {
                MLog.w(TAG, "duplicate registerSD for " + clazz.getSimpleName());
            }
        }
    }

    @Override
    public void unregisterSignalDispatcher(Class clazz) {
        synchronized (dispatcherListLock) {
            if(mCustomDispatchers.containsKey(clazz)) {
                mCustomDispatchers.remove(clazz);
            }
        }
    }

    private ResponseDispatcher getResponseDispatcher() {
        return new ResponseDispatcher() {
            @Override
            public void dispatch(TlvSignal signal) {
                if(MLog.enable) {
                    if(signal instanceof VoiceMessageResp) {
                        MLog.w(TAG, "VoiceMessageResp " + signal.toString());
                    } else if(signal instanceof VoiceMessageNotify) {
                        //不打印，太多了
                        //MLog.d(TAG, "dispatch VoiceMessageNotify");
                    } else if(signal instanceof TextMessageNotify) {
                        MLog.d(TAG, "response to dispatch:" + signal.getClass().getSimpleName()
                                + "," +((TextMessageNotify) signal).getText());
                    } else {
                        MLog.d(TAG, "response to dispatch:" + signal.getClass().getSimpleName());
                    }
                }
                synchronized (dispatcherListLock) {

                    if(mMessageFilter != null) {
                        if(signal instanceof VoiceMessageNotify) {
                            VoiceMessageNotify notify = (VoiceMessageNotify) signal;
                            if(mMessageFilter.filterVoiceMsg(notify.getYunvaId(), notify.getExpand())) {
                                return;
                            }
                        } else if(signal instanceof TextMessageNotify) {
                            TextMessageNotify notify = (TextMessageNotify) signal;
                            if(mMessageFilter.filterTextMsg(notify.getYunvaId(),
                                    notify.getText(), notify.getExpand())) {
                                return;
                            }
                        }
                    }

                    if (!mCustomDispatchers.isEmpty()) {
                        final Class c = signal.getClass();
                        if(mCustomDispatchers.containsKey(c)) {
                            ResponseDispatcher dispatcher = mCustomDispatchers.get(c);
                            if(dispatcher != null) {
                                try {
                                    dispatcher.dispatch(signal);
                                } catch (Exception e) {
                                    MLog.e(TAG, "Dispatcher:" + dispatcher.getClass().getName() + "|Signal:"
                                    + signal.getClass().getName());
                                    MLog.e(TAG, "Exception Caught while dispatching :" + e.getMessage());
                                    e.printStackTrace();
                                    if(mTcpExceptionHandler != null) {
                                        mTcpExceptionHandler.onTcpException(
                                                TcpExceptionHandler.SIGNAL_DISPATCH_EXCEPTION, e);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
                defaultDispatchResponse(signal);
            }
        };
    }

    private TcpExceptionHandler getTcpExceptionHandler() {
        return new TcpExceptionHandler() {
            @Override
            public void onTcpException(final int code, final Exception e) {
                MLog.e("TcpException", code + "," + e.getClass().getSimpleName() + ":" + e.getMessage());
                synchronized (exceptionHandlerListLock) {
                    for(final TcpExceptionHandler handler : mTcpExceptionHandlerList) {
                        if(mHandler != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    handler.onTcpException(code, e);
                                }
                            });
                        }
                    }
                }
            }
        };
    }

    private void defaultDispatchResponse(final TlvSignal signal) {
        long reqSeq = signal.getHeader().getMessageId();
        if(!mInflightRequests.containsKey(reqSeq)) {
            //canceled before response
            return;
        }

        TcpRequest request = null;
        synchronized (this) {
            if(mInflightRequests.containsKey(reqSeq)) {
                request = mInflightRequests.get(reqSeq);
                mInflightRequests.remove(reqSeq);
            }
        }

        if(request != null) {
            final TlvSignal req = request.getTlvSignal();
            final TcpResponseCallback callback = request.getTcpResponseCallback();
            if(callback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTcpResponse(req, signal);
                    }
                });
            }
        }
    }

    private class LocalHandler extends Handler {
        private static final int MSG_TIMEOUT = 111;

        LocalHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_TIMEOUT) {
                long seq = (long) msg.obj;
                MLog.d("TimeoutHandler", "check timeout event seq=" + seq);
                TcpRequest request = null;
                synchronized (YayaTcp.this) {
                    if(mInflightRequests.containsKey(seq)) {
                        request = mInflightRequests.get(seq);
                        mInflightRequests.remove(seq);
                    }
                }
                if(request != null) {
                    final TcpTimeoutCallback callback = request.getTcpTimeoutCallback();
                    final TlvSignal req = request.getTlvSignal();
                    if(callback != null) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSignalTimeout(req);
                            }
                        });
                    }
                }
            }
        }

        void recordTimeout(long seq, long timeout) {
            Message msg = obtainMessage();
            msg.what = MSG_TIMEOUT;
            msg.obj = seq;
            sendMessageDelayed(msg, timeout);
        }

        void removeTimeoutRecord(long seq) {
            removeMessages(MSG_TIMEOUT, seq);
        }
    }

}
