package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0005)
public class LogoutReq extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 3)
    private String troopsId;
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	@Override
	public String toString() {
		return "LogoutVideoReq [yunvaId=" + yunvaId + ", troopsId=" + troopsId + "]";
	}

}
