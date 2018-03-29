package com.yaya.sdk;

import com.yaya.sdk.account.AccountAuthCallback;
import com.yaya.sdk.tcp.TcpExceptionHandler;

/**
 * Created by ober on 2016/11/15.
 */
public interface ExceptionRecovery {

    void notifyEnterRoomResult(int code, Exception e);

    interface RetryCallback {
        void onReconnectStart();
        void onReconnectFail(int errCode, String msg);
        void onReconnectSuccess();
    }

    //备案一些重连逻辑, 开始监测tcp异常，自动重连
    void prepare(String roomSeq, RetryCallback callback, AccountAuthCallback authCallback);

    //取消tcp异常监测，不再重连
    void cancel();

    //手动重连
    void reconnect(int code, String msg);
}
