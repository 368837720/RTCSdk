package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-4-1.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x0009)
public class MicReq extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 2)
    private String actionType;//1上麦；0下麦
    @TlvSignalField(tag = 3)
    private String troopsId;
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	@Override
	public String toString() {
		return "MicReq [yunvaId=" + yunvaId + ", actionType=" + actionType
				+ ", troopsId=" + troopsId + "]";
	}

}
