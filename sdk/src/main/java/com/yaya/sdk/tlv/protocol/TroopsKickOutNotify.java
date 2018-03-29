package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-3-22.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x0011)
public class TroopsKickOutNotify extends TlvSignal {
	@TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
	private Long yunvaId;
	@TlvSignalField(tag = 2)
	private String troopsId;
	@TlvSignalField(tag = 3)
	private String msg;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "TroopsKickOutNotify [yunvaId=" + yunvaId + ", troopsId="
				+ troopsId + ", msg=" + msg + "]";
	}
	
}
