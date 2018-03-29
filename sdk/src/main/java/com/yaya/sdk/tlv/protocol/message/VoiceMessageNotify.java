package com.yaya.sdk.tlv.protocol.message;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-3-16.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x1011)
public class VoiceMessageNotify extends TlvSignal {
    @TlvSignalField(tag = 1)
    private String troopsId;
    @TlvSignalField(tag = 2, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 3)
    private byte[] msg;
    @TlvSignalField(tag = 4)
    private String expand;
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public byte[] getMsg() {
		return msg;
	}
	public void setMsg(byte[] msg) {
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
		return "VoiceMessageNotify [troopsId=" + troopsId + ", yunvaId="
				+ yunvaId + ", msg=" + msg + ", expand="
				+ expand + "]";
	}

}
