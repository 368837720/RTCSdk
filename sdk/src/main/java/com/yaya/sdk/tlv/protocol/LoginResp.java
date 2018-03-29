package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0004)
public class LoginResp extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long result;
    @TlvSignalField(tag = 2)
    private String msg;
    @TlvSignalField(tag = 3)
    private Byte mode; //0自由，1抢麦
    @TlvSignalField(tag = 4)
    private Byte leaderMode; //0表示是没有队长，1表示有队长
    @TlvSignalField(tag = 5)
    private Long leaderId;

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

    public Byte getMode() {
		return mode;
	}

	public void setMode(Byte mode) {
		this.mode = mode;
	}

	public Byte getLeaderMode() {
		return leaderMode;
	}

	public void setLeaderMode(Byte leaderMode) {
		this.leaderMode = leaderMode;
	}

	public Long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	@Override
	public String toString() {
		return "LoginTroopsResp [result=" + result + ", msg=" + msg + ", mode="
				+ mode + ", leaderMode=" + leaderMode + ", leaderId="
				+ leaderId + "]";
	}

}
