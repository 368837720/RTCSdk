package com.yaya.sdk.tcp;

import yaya.tlv.signal.TlvSignal;

/**
 * Tcp response的tlvSignal 分发器， 运行在tcp读写线程上， 不建议进行耗时操作
 * ，出线异常会导致tcp线程挂掉!
 * Created by ober on 2016/11/8.
 */
public interface ResponseDispatcher {

    void dispatch(TlvSignal signal);
}
