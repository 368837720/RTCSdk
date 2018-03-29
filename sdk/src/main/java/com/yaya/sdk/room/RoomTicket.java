package com.yaya.sdk.room;

import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;

/**
 * 进入房间的ticket
 * Created by ober on 2016/11/12.
 */
public class RoomTicket {
    private final GetTroopsInfoResp troopsInfo;
    private final byte mode;  //mode 0自由 1抢麦 2指挥
    private final String seq;

    private boolean isUsed; //ticket只能用一次

    private String expandForVoice; //扩展字段

    public RoomTicket(GetTroopsInfoResp troopsInfo, byte mode, String seq) {
        this.troopsInfo = troopsInfo;
        this.mode = mode;
        this.seq = seq;
    }

    public byte getMode() {
        return mode;
    }

    public GetTroopsInfoResp getTroopsInfo() {
        return troopsInfo;
    }

    public void setExpandForVoice(String expandForVoice) {
        this.expandForVoice = expandForVoice;
    }

    public String getSeq() {
        return seq;
    }

    public void setUsed() {
        isUsed = true;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public String getExpandForVoice() {
        return expandForVoice;
    }
}
