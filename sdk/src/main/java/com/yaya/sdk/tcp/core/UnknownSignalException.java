package com.yaya.sdk.tcp.core;

/**
 * Created by ober on 2016/11/7.
 */
class UnknownSignalException extends Exception {
    public UnknownSignalException(){
        super();
    }
    public UnknownSignalException(String msg) {
        super(msg);
    }
}
