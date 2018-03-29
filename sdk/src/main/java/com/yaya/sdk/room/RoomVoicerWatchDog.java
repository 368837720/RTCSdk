package com.yaya.sdk.room;

import com.yaya.sdk.RTV;
import com.yaya.sdk.tlv.protocol.LogoutResp;
import com.yaya.sdk.util.ExceptionUtil;

/**
 * 监听RoomVoicer的行为
 * Created by ober on 2016/11/12.
 */
public interface RoomVoicerWatchDog {
    int CODE_SUCCESS = 0;

    int CODE_BAD_TICKET = 211;
    int CODE_ALREADY_IN_ROOM = 212;
    int CODE_UNKNOWN_STATE = 213;

    int CODE_CONNNECT_FAIL = 215;

    int CODE_MODE_SET_FAIL = 111;
    int CODE_LOGIN_TIMEOUT = 112;

    int CODE_DUPLICATE_MIC_UP = 113;
    int CODE_DUPLICATE_MIC_DOWN = 114;

    int CODE_MIC_UP_ERR_RESP = 115;
    int CODE_MIC_UP_TIMEOUT = 119;
    int CODE_MIC_DOWN_ERR_RESP = 116;
    int CODE_MIC_DOWN_TIMEOUT = 118;

    int CODE_NOT_IN_ROOM = 117;

    void enterRoomCallback(int code, Exception e);
    void quitRoomCallback(LogoutResp resp);

    void micUpCallback(int code, Exception e);
    void micDownCallback(int code, Exception e);

    void sendTextMsgCallback(int code, Exception e, String expand);

    void recordException(int recordErrCode);

    void onModeChangeCallback(int code, String msg, RTV.Mode mode);
}
