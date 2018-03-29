package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0016)
public class ModeSettingResp extends TlvSignal {
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
		return "ModeSettingResp [result=" + result + ", msg=" + msg + "]";
	}

}
