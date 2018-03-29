package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0017)
public class ModeChangeNotify extends TlvSignal {
    @TlvSignalField(tag = 4)
    private Byte mode; //0自由，1抢麦
    @TlvSignalField(tag = 1)
    private Byte LeaderMode; //0表示是没有队长，1表示有队长
    @TlvSignalField(tag = 2)
    private Long yunvaId;
    @TlvSignalField(tag = 3)
    private String nickname;
	public Byte getMode() {
		return mode;
	}
	public void setMode(Byte mode) {
		this.mode = mode;
	}
	public Byte getLeaderMode() {
		return LeaderMode;
	}
	public void setLeaderMode(Byte leaderMode) {
		LeaderMode = leaderMode;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Override
	public String toString() {
		return "ModeChangeNotify [mode=" + mode + ", LeaderMode=" + LeaderMode
				+ ", yunvaId=" + yunvaId + ", nickname=" + nickname + "]";
	}

}
