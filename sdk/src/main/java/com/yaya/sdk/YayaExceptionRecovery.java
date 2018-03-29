package com.yaya.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yaya.sdk.account.AccountAuthCallback;
import com.yaya.sdk.account.AccountState;
import com.yaya.sdk.account.auth.ThirdAuth;
import com.yaya.sdk.account.auth.YayaAuth;
import com.yaya.sdk.audio.PlayManager;
import com.yaya.sdk.audio.RecordManager;
import com.yaya.sdk.tcp.ITcpConnection;
import com.yaya.sdk.tcp.TcpExceptionHandler;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.info.AuthResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthResp;
import com.yaya.sdk.util.NetworkUtil;

/**
 * tcp连接异常之后, 执行重连，重新auth-->get troopsInfo-->tcp connect-->login req
 * Created by ober on 2016/11/15.
 */
public class YayaExceptionRecovery implements ExceptionRecovery, TcpExceptionHandler {

    private static final String TAG = "YayaExceptionRecovery";

    private String mRoomSeq; //之前登录的房间seq
    private RetryCallback mRetryCallback;
    private AccountAuthCallback mAuthCallback;

    private static final int RETRY_COUNT_DEF = 3;
    private static final long RETRY_TIMEOUT_DEF = 60 * 1000 * 2;

    private final int retryCount;
    private final long retryTimeout;

    private final RetryCountHandler mRetryCountHandler;
    private final Context mContext;
    private final YayaAuth mYayaAuth;
    private final ThirdAuth mThirdAuth;
    private volatile boolean isReconnecting; //正在重连

    static ExceptionRecovery newInstance(Context c, YayaAuth yayaAuth, ThirdAuth thirdAuth, Integer retryCount, Long timeout) {
        if(retryCount == null) {
            retryCount = RETRY_COUNT_DEF;
        }
        if(timeout == null) {
            timeout = RETRY_TIMEOUT_DEF;
        }
        return new YayaExceptionRecovery(c, yayaAuth, thirdAuth, retryCount, timeout);
    }

    YayaExceptionRecovery(Context c, YayaAuth yayaAuth, ThirdAuth thirdAuth, int retryCount, long timeout) {
        this.retryCount = retryCount;
        this.retryTimeout = timeout;
        this.mContext = c;
        this.mRetryCountHandler = new RetryCountHandler(timeout);
        this.mYayaAuth = yayaAuth;
        this.mThirdAuth = thirdAuth;
    }

    @Override
    public void prepare(String roomSeq, RetryCallback callback, AccountAuthCallback authCallback) {
        mRoomSeq = roomSeq;
        mRetryCallback = callback;
        mAuthCallback = authCallback;
        YayaTcp.getInstance().registerExceptionHandler(this);
        isReconnecting = false;
    }

    @Override
    public void cancel() {
        mRoomSeq = null;
        YayaTcp.getInstance().unregisterExceptionHandler(this);
        isReconnecting = false;
    }

    private boolean isCanceled() {
        return mRoomSeq == null;
    }

    @Override
    public void reconnect(int code, String msg) {
        MLog.d(TAG, "reconnect code=" + code + ",msg=" + msg);
        if(isReconnecting) {
            //防止收到多个重连请求
            MLog.w(TAG, "duplicate reconnect ignored while it is reconnecting...");
            return;
        }

        final RetryCallback retryCallback = mRetryCallback;
        if(mRetryCountHandler.getHasRetryCount() >= retryCount) {
            //超过重连次数
            MLog.e(TAG, "no more retry count, fail! code=" + code + ",msg=" + msg);
            mRetryCountHandler.clear();
            if(retryCallback != null) {
                isReconnecting = false;
                retryCallback.onReconnectFail(code, msg);
            }
            return;
        }

        MLog.w(TAG, "reconnect code=" + code +",msg=" + msg);
        realReconnect(code, msg);
    }

    @Override
    public void notifyEnterRoomResult(int code, Exception e) {
        if(!isReconnecting) {
            //没有在重连，跟重连逻辑无关
            return;
        }
        isReconnecting = false;
        RetryCallback callback = mRetryCallback;

        if(code == 0) {
            MLog.i(TAG, "retry connect success");
            //成功了

            if(callback != null) {
                callback.onReconnectSuccess();
            }
        } else {
            isReconnecting = false;
            MLog.e(TAG, "retry connect fail code=" + code + ","
                    + e.getClass().getSimpleName() + ":" + e.getMessage());
            if(callback != null && mRetryCountHandler.getHasRetryCount() >= retryCount) {
                RecordManager.getInstance().stopRecord();
                PlayManager.getInstance().stopListenAndPlay();

                callback.onReconnectFail(code, e.getMessage());
            }
        }

    }

    @Override
    public void onTcpException(int code, Exception e) {
        if(isCanceled()) {
            return;
        }
        if(code == CHANNEL_CLOSE_EXCEPTION ||
                code == SOCKET_CLOSE_EXCEPTION ||
                code == SELECTOR_CLOSE_EXCEPTION) {
            //忽略disconnect时的异常， 不需要重连
            MLog.w(TAG, "ignore tcpException code=" + code + ","
                    + e.getClass().getSimpleName() + ":" + e.getMessage());
            return;
        }
        if(code == WRITE_BUFFER_OVER_FLOW) {
            MLog.w(TAG, "reconnect cause: WRITE_BUFFER_OVER_FLOW");
            //return;
        }
        if(code == TLV_NOT_IMPLEMENT) {
            return;
        }
        reconnect(code, e.getMessage());
    }

    private void realReconnect(final int code, String msg) {
        Looper looper = YayaTcp.getInstance().getYayaThreadLooper();
        if(looper == null) {
            //YayaThread 未初始化，或已经退出, 或者挂掉了
            MLog.w(TAG, "yayaThread not exists, ignore this reconnect call");
            return;
        }

        //在YayaThread上执行重连操作
        isReconnecting = true;
        mRetryCountHandler.recordRetryCount();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (NetworkUtil.isNetworkAvailable(mContext) && NetworkUtil.isNetworkConnected(mContext)) {
            new Handler(looper).postDelayed(realReconnectTask, 100);
        } else {
            //// TODO: 2016/11/15  这里要不要回调给用户？
            MLog.w(TAG, "当前网络不可用,8000ms后重连");
            Handler yayaHandler = new Handler(looper);
            yayaHandler.post(disconnectTask);
            yayaHandler.postDelayed(realReconnectTask, 8000);
        }

    }

    private Runnable disconnectTask = new Runnable() {
        @Override
        public void run() {
            //如果连接还在，关闭当前连接
            YayaTcp.getInstance().cancelAllRequest();
            ITcpConnection conn = YayaTcp.getInstance().getConnection();
            if(conn != null && conn.isConnected()) {
                conn.disconnect();
            }

            //play线程 和 record线程 如果在运行，则停止，回复到初始状态
            if(RecordManager.getInstance().isRecording()) {
                RecordManager.getInstance().stopRecord();
            }
            if(PlayManager.getInstance().isPlaying()) {
                PlayManager.getInstance().stopListenAndPlay();
            }
            RecordManager.getInstance().destroy();
            PlayManager.getInstance().destroy();
        }
    };

    private Runnable realReconnectTask = new Runnable() {
        @Override
        public void run() {

            final RetryCallback callback = mRetryCallback;
            final AccountAuthCallback authCallback = mAuthCallback;
            //如果连接还在，关闭当前连接
            YayaTcp.getInstance().cancelAllRequest();
            ITcpConnection conn = YayaTcp.getInstance().getConnection();
            if(conn != null && conn.isConnected()) {
                conn.disconnect();
            }

            //play线程 和 record线程 如果在运行，则停止，回复到初始状态
            if(RecordManager.getInstance().isRecording()) {
                RecordManager.getInstance().stopRecord();
            }
            if(PlayManager.getInstance().isPlaying()) {
                PlayManager.getInstance().stopListenAndPlay();
            }
            RecordManager.getInstance().destroy();
            PlayManager.getInstance().destroy();

            final String targetRoomSeq = mRoomSeq;
            if(targetRoomSeq == null) {
                //异常状态, 不应该到达这里
                MLog.e(TAG, "ReconnectTask 没有找到之前的房间seq 重连失败");
                isReconnecting = false;
                if(callback != null) {
                    callback.onReconnectFail(-1, "没有找到之前的房间seq");
                }
                return;
            }
            if(authCallback == null) {
                MLog.w(TAG, "authCallback 已经被回收，不再重连");
                return;
            }
            //获取账户状态
            AuthResp authResp = AccountState.getInstance().getAuthResp();
            ThirdAuthResp thirdAuthResp = AccountState.getInstance().getThirdAuthResp();
            String tt = AccountState.getInstance().getTt();
            if(authResp != null) {
                //之前帐号已经通过yayaAuth
                if(callback != null) {
                    callback.onReconnectStart();
                }
                mYayaAuth.auth(authCallback);
            } else if(thirdAuthResp != null && tt != null) {
                //之前帐号已通过thirdAuth
                if(callback != null) {
                    callback.onReconnectStart();
                }
                mThirdAuth.auth(AccountState.getInstance().getTt(), authCallback);
            } else {
                //异常状态, 不应该到达这里
                MLog.e(TAG, "ReconnectTask 没有找到之前的账户信息 重连失败");
                if(callback != null) {
                    isReconnecting = false;
                    callback.onReconnectFail(-1, "没有找到账户信息");
                }
            }
        }
    };

    //维护retryCount,{RETRY_TIMEOUT_DEF}时间内最多重连{RETRY_COUNT_DEF}次，如果还不成功，则不再重连
    private static class RetryCountHandler extends Handler {
        int hasRetryCount = 0;
        long retryTimeout;

        RetryCountHandler(long timeout) {
            super(Looper.getMainLooper());
            this.retryTimeout = timeout;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                hasRetryCount = 0;
            }
        }

        void clear() {
            hasRetryCount = 0;
            removeMessages(1);
        }

        void recordRetryCount() {
            if(hasRetryCount == 0) {
                sendEmptyMessageDelayed(1, retryTimeout);
            }
            hasRetryCount ++;
        }

        int getHasRetryCount() {
            return hasRetryCount;
        }
    }
}
