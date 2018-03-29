package com.yaya.sdk.tlv.protocol.message;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-3-14.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x1008)
public class VoiceMessageResp extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long result;
    @TlvSignalField(tag = 2)
    private String msg;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("result:").append(result);
        sb.append("|msg:").append(msg);
        return sb.toString();
    }
}
