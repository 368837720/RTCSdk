package com.yaya.sdk.tcp;

/**
 * Created by ober on 2016/11/7.
 */
public interface TcpExceptionHandler {

    int SELECT_EXCEPTION = 1;  //selector.select异常
    int SOCKET_READ_EXCEPTION = 2; //socketChannel.read异常
    int SOCKET_READ_NEGATIVE = 3; //socketChannel.read 返回 -1, 连接已关闭
    int TLV_DECODE_HEADER_EXCEPTION = 4; //socket read时解析tlvAccessHeader异常
    int TLV_BODY_DECOMPRESS_EXCEPTION = 5; //socket read时解析tlvBody decompress异常
    int TLV_SIGNAL_DECODE_EXCEPTION = 6; //socket read时解析tlvBody异常
    int TLV_NOT_IMPLEMENT = 7; //socket read时解析到未在客户端实现的tlv msgCode
    int SOCKET_WRITE_EXCEPTION = 8; //socketChannel.write异常
    int WRITE_BUFFER_OVER_FLOW = 9;//写buffer溢出，通道被阻塞了

    //connect时异常
    int CHANNEL_OPEN_EXCEPTION = 10;//selectorProvider.openSocketChannel()异常
    int SOCKET_CONFIG_EXCEPTION = 11; //socket设置参数异常
    int SOCKET_CONN_TIME_OUT = 12; //socket.connect SocketTimeoutException
    int SOCKET_CONN_EXCEPTION = 13;//socket.connect 其它IOException
    int CHANNEL_CLOSED_UNEXPECTED_EXCEPTION = 14; //socketChannel.register channel关闭异常
    int SOCKET_CONFIG_BLOCKING_EXCEPTION = 15;//socketChannel.configureBlocking(false) 异常

    int TLV_ENCODE_EXCEPTION = 19;  //写操作时 tlv encode异常
    int WAIT_HEART_BEAT_TIMEOUT = 21; //等待HeartBeat Response超时

    int SIGNAL_DISPATCH_EXCEPTION = 20; //signal分发异常

    //disconnect异常
    int SOCKET_CLOSE_EXCEPTION = 16; //socket.close 异常
    int CHANNEL_CLOSE_EXCEPTION = 17; //socketChannel.close 异常
    int SELECTOR_CLOSE_EXCEPTION = 18; //selector.close 异常


    /**
     * call on Yaya Thrad
     * @param code
     * @param e
     */
    void onTcpException(int code, Exception e);
}
