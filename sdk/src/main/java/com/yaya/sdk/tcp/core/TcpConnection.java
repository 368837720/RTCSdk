package com.yaya.sdk.tcp.core;

import android.os.Build;
import android.os.Looper;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tcp.ITcpConnection;
import com.yaya.sdk.YayaNetStateListener;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.SeqUtil;
import com.yaya.sdk.tcp.TcpConfig;
import com.yaya.sdk.tcp.TcpExceptionHandler;
import com.yaya.sdk.tlv.TlvUtil;
import com.yaya.sdk.tlv.YayaTlvStore;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.AccessControlException;

import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.signal.TlvSignal;
import yaya.tlv.util.TlvCodecUtil;

/**
 * Created by ober on 2016/11/7.
 */
class TcpConnection implements ITcpConnection {

    private final static String TAG = "TcpConnection";

    static {
        try {
            // Needed for NIO selectors on Android 2.2.
            if(Build.VERSION.SDK_INT < 10)
                System.setProperty("java.net.preferIPv6Addresses", "false");
        } catch (AccessControlException ignored) {
        }
    }

    private final int mObjectBufferSize, mWriteBufferSize;

    private SocketChannel mSocketChannel;
    private SelectionKey mSelectionKey;
    private ReadThread mReadThread;

    private final TcpExceptionHandler mTcpExceptionHandler;
    private final ResponseDispatcher mRespDispatcher;

    private Selector mSelector;
    private final RtcHeart mRtcHeart;

    TcpConnection(ResponseDispatcher responseDispatcher,
                  TcpExceptionHandler exceptionHandler) {
        this.mObjectBufferSize = TcpConfig.LOCAL_READ_BUFFER_SIZE;
        this.mWriteBufferSize = TcpConfig.LOCAL_WRITE_BUFFER_SIZE;
        mTcpExceptionHandler = exceptionHandler;
        mRespDispatcher = responseDispatcher;
        mRtcHeart = new RtcHeart(getHeartBeatCallback());
    }

    @Override
    public void connect(SocketAddress remoteAddress) {
        MLog.d(TAG, "try connect " + remoteAddress.toString());

        try {
            mSelector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(mReadThread != null) {
            if(mReadThread.isRunning()) {
                MLog.w(TAG, "readThread is running, quit safely");
                mReadThread.quitSafely();
            }
        }
        mSelector.wakeup();

        try {
            mSocketChannel = mSelector.provider()
                    .openSocketChannel();
        } catch (IOException e) {
            MLog.e(TAG, e.getMessage());
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.CHANNEL_OPEN_EXCEPTION, e);
            return;
        }
        Socket socket = mSocketChannel.socket();
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(TcpConfig.SO_TIMEOUT_MILLIS);
            socket.setTcpNoDelay(true);
            socket.setOOBInline(true);
            socket.setReuseAddress(false);
            socket.setSoLinger(false, 0);
            socket.setReceiveBufferSize(TcpConfig.TCP_RECV_BUFFER_SIZE);
            socket.setSendBufferSize(TcpConfig.TCP_SEND_BUFFER_SIZE);
        } catch (SocketException e) {
            MLog.e(TAG, e.getMessage());
            try {
                mSocketChannel.close();
                mSocketChannel = null;
            } catch (IOException ignore) {
                MLog.e(TAG, ignore.getMessage());
            }
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.SOCKET_CONFIG_EXCEPTION, e);
            return;
        }

        try {
            MLog.d(TAG, "connect...");
            socket.connect(remoteAddress, TcpConfig.SO_CONN_TIMEOUT_MILLIS);
            MLog.d(TAG, "connect success! port=" + socket.getLocalPort());
        } catch (SocketTimeoutException e) {
            MLog.e(TAG, e.getMessage());
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.SOCKET_CONN_TIME_OUT, e);
            return;
        } catch (IOException e) {
            MLog.e(TAG, e.getMessage());
            try {
                socket.close();
                mSocketChannel.close();
                mSocketChannel = null;
            } catch (IOException ignore) {
                MLog.e(TAG, ignore.getMessage());
            }
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.SOCKET_CONN_EXCEPTION, e);
            return;
        }

        try {
            mSocketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
                mSocketChannel.close();
                mSocketChannel = null;
            } catch (IOException ignore) {
                MLog.e(TAG, ignore.getMessage());
            }
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.SOCKET_CONFIG_BLOCKING_EXCEPTION, e);
            return;
        }

        try {
            mSelectionKey = mSocketChannel.register(mSelector,
                        SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            MLog.e(TAG, e.getMessage());
            try {
                socket.close();
                mSocketChannel.close();
                mSocketChannel = null;
            } catch (IOException ignore) {
                MLog.e(TAG, ignore.getMessage());
            }
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.CHANNEL_CLOSED_UNEXPECTED_EXCEPTION, e);
            return;
        }

        mSelectionKey.attach(this);

        mReadThread = new ReadThread(mSocketChannel, mSelector,
                mSelectionKey, mObjectBufferSize, mWriteBufferSize,
                mRespDispatcher, mTcpExceptionHandler);
        mReadThread.setName("TcpThread");
        mReadThread.start();
        mRtcHeart.startBeat(YayaTcp.getInstance().getYayaThreadLooper());
    }

    @Override
    public void disconnect() {
        MLog.d(TAG, "disconnect");
        mRtcHeart.stopBeat();
        if(mSelector != null) {
            mSelector.wakeup();
        }
        if(mReadThread != null) {
            mReadThread.quitSafely();
            mReadThread = null;
        }
        if(mSocketChannel != null) {
            try {
                mSocketChannel.socket().close();
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                mTcpExceptionHandler.onTcpException(
                        TcpExceptionHandler.SOCKET_CLOSE_EXCEPTION, e);
                return;
            }

            try {
                if(mSocketChannel != null) {
                    mSocketChannel.close();
                    mSocketChannel = null;
                }
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                mTcpExceptionHandler.onTcpException(
                        TcpExceptionHandler.CHANNEL_CLOSE_EXCEPTION, e);
                return;

            }
            if(mSelectionKey != null) {
                mSelectionKey.selector().wakeup();
                mSelectionKey.cancel();
            }
            try {
                mSelector.close();
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                mTcpExceptionHandler.onTcpException(
                        TcpExceptionHandler.SELECTOR_CLOSE_EXCEPTION, e);
            }
        }
    }

    @Override
    public boolean isConnected() {
        if(mReadThread == null || mSocketChannel == null) {
            return false;
        }
        Socket socket = mSocketChannel.socket();
        return mSocketChannel.isConnected()
                && socket.isConnected()
                && !socket.isClosed();
    }

    /**
     * be certain tcp is connected before write
     */
    @Override
    public void write(byte[] data) {
        if(!isConnected()) {
            MLog.e(TAG, "try write data to dead connection, write operation ignored!");
            return;
        }
        mReadThread.send(data);
    }

    @Override
    public void write(TlvSignal tlvSignal) {
        if(tlvSignal.getHeader() == null) {
            TlvAccessHeader header = TlvUtil.buildHeader(SeqUtil.newSeq(),
                    TlvUtil.getModuleId(tlvSignal), TlvUtil.getMsgCode(tlvSignal));
            tlvSignal.setHeader(header);
        }
        byte[] data;
        try {
            data = TlvCodecUtil.encodeSignal(tlvSignal, YayaTlvStore.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            mTcpExceptionHandler.onTcpException(
                    TcpExceptionHandler.TLV_ENCODE_EXCEPTION, e);
            return;
        }
        write(data);
    }

    private YayaNetStateListener outHeartBeatCallback;

    @Override
    public void setHeartBeatCallback(YayaNetStateListener callback) {
        outHeartBeatCallback = callback;
    }

    private HeartBeatCallback getHeartBeatCallback() {
        return new HeartBeatCallback() {
            @Override
            public void onWaitHeartBeatResponseTimeout() {
                MLog.e(TAG, "onWaitHeartBeatResponseTimeout !");
                mTcpExceptionHandler.onTcpException(TcpExceptionHandler.WAIT_HEART_BEAT_TIMEOUT,
                        new SocketTimeoutException("wait heart beat resp time out"));
            }

            @Override
            public void onHeartBeatResponse(long sendTime, long respTime) {
                MLog.d(TAG, "onHeartBeatResponse elapse=" + (respTime - sendTime));
                if(outHeartBeatCallback != null) {
                    outHeartBeatCallback.onNetStateUpdate(sendTime, respTime);
                }
            }
        };
    }
}
