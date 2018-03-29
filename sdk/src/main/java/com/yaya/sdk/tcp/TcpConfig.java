package com.yaya.sdk.tcp;

/**
 * Created by ober on 2016/11/7.
 */
public final class TcpConfig {
    public static final int SO_TIMEOUT_MILLIS = 10000;

    public static final int SO_CONN_TIMEOUT_MILLIS = 3000;

    public static final int TCP_RECV_BUFFER_SIZE = 64 * 1024;
    public static final int TCP_SEND_BUFFER_SIZE = 64 * 1024;

    public static final int LOCAL_READ_BUFFER_SIZE = 8192;
    public static final int LOCAL_WRITE_BUFFER_SIZE = 8192;

    public static final long HEART_BEAT_TIME_OUT = 60000;
    public static final long HEART_BEAT_PERIOD = 60000;
}
