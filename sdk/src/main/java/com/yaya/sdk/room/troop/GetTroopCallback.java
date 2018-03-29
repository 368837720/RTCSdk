package com.yaya.sdk.room.troop;

import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;

/**
 * Created by ober on 2016/10/28.
 */
public interface GetTroopCallback {

    int CODE_REQUEST_FAIL = 11;
    int CODE_DECODE_FAIL = 12;
    int CODE_RESPONSE_ERR = 13;

    void onGetTroopSuccess(String seq, GetTroopsInfoResp resp);
    void onGetTroopFail(int code, String msg);
}

