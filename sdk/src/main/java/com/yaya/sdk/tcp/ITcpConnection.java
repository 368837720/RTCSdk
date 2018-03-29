package com.yaya.sdk.tcp;

import com.yaya.sdk.YayaNetStateListener;

import java.net.SocketAddress;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/10.
 */
public interface ITcpConnection {
    /**
     * sync connect
     *
     * throws
     *  {@link TcpExceptionHandler#CHANNEL_OPEN_EXCEPTION}
     *  {@link TcpExceptionHandler#SOCKET_CONFIG_EXCEPTION}
     *  {@link TcpExceptionHandler#SOCKET_CONN_EXCEPTION}
     *  {@link TcpExceptionHandler#SOCKET_CONFIG_BLOCKING_EXCEPTION}
     *  {@link TcpExceptionHandler#SOCKET_CONN_TIME_OUT}
     *  {@link TcpExceptionHandler#CHANNEL_CLOSED_UNEXPECTED_EXCEPTION}
     *
     * @param remoteAddress address to connect, not null
     */
    void connect(SocketAddress remoteAddress);

    /**
     * sync disconnect
     */
    void disconnect();
    boolean isConnected();

    /**
     * sync write
     * @param data
     */
    void write(byte[] data);

    /**
     * sync write
     * @param tlvSignal
     */
    void write(TlvSignal tlvSignal);

    void setHeartBeatCallback(YayaNetStateListener callback);
}
