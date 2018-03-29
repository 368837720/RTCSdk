package com.yaya.sdk.account;

/**
 * 注：callback 的线程可能是不同的, 注意处理
 * Created by ober on 2016/10/26.
 */
public interface AccountAuthCallback {

    int CODE_GMGC_REQUEST_FAIL = 11; //gmgc 请求失败
    int CODE_GMGC_DECODE_ERR = 12; //gmgc decode失败
    int CODE_GMGC_RESPONSE_ERR = 13; //gmgc 返回异常错误码

    int CODE_3AUTH_REQUEST_FAIL = 14;
    int CODE_3AUTH_DECODE_ERR = 15;
    int CODE_3AUTH_RESPONSE_ERR = 16;

    int CODE_REGISTER_REQUEST_FAIL = 17;
    int CODE_REGISTER_DECODE_ERR = 18;
    int CODE_REGISTER_RESPONSE_ERR = 19;

    int CODE_AUTH_REQUEST_FAIL = 20;
    int CODE_AUTH_DECODE_ERR = 21;
    int CODE_AUTH_RESPONSE_ERR = 22;

    int CODE_AUTH_NO_STORE = 23;

    void onAuthSuccess();

    void onAuthFailed(int code, String msg);

}
