package com.yaya.sdk.tlv.protocol.message;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-3-14.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x1006)
public class TextMessageResp extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long result;
    @TlvSignalField(tag = 2)
    private String msg;
    
    private String expand;

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

    public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	@Override
	public String toString() {
		return "TextMessageResp [result=" + result + ", msg=" + msg
				+ ", expand=" + expand + "]";
	}

}
