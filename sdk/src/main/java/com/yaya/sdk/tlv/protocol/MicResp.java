package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-4-1.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x0010)
public class MicResp extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long result;
    @TlvSignalField(tag = 2)
    private String msg;
    @TlvSignalField(tag = 3)
    private String actionType;

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("result:").append(result);
        sb.append("|msg:").append(msg);
        sb.append("|actionType:").append(actionType);
        return sb.toString();
    }
}
