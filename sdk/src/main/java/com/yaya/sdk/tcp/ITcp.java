package com.yaya.sdk.tcp;

import android.os.Looper;

import com.yaya.sdk.MessageFilter;
import com.yaya.sdk.tcp.core.TcpRequest;

/**
 * Created by ober on 2016/11/9.
 */
public interface ITcp {

    void open();

    void setMessageFilter(MessageFilter filter);

    void close();

    Looper getYayaThreadLooper();

    ITcpConnection getConnection();

    long sendAndListen(TcpRequest request);

    long sendAndListen(TcpRequest request, long timeout);

    void cancelRequest(long seqNum);

    void cancelAllRequest();

    void registerExceptionHandler(TcpExceptionHandler handler);

    void unregisterExceptionHandler(TcpExceptionHandler handler);

    void registerSignalDispatcher(Class clazz, ResponseDispatcher dispatcher);

    void unregisterSignalDispatcher(Class clazz);
}
