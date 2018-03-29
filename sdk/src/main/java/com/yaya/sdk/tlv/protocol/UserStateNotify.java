package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0013)
public class UserStateNotify extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 2)
    private String troopsId;
    @TlvSignalField(tag = 3)
    private Byte type;   //1是send,2是receiver
    @TlvSignalField(tag = 4)    //00表示全空，右边第一位是文字，左边是实时语音
    private String state;
    @TlvSignalField(tag = 5)
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
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "UserStateNotify [yunvaId=" + yunvaId + ", troopsId=" + troopsId
				+ ", type=" + type + ", state=" + state + ", msg=" + msg + "]";
	}

}
