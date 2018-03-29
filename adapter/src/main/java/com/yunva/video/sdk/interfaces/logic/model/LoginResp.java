package com.yunva.video.sdk.interfaces.logic.model;


public class LoginResp {
    private Long result;
    private String msg;
    private Long yunvaId;
    private int modeType; //0自由模式，1抢麦模式，2指挥模式
    private boolean isLeader; //是否是队长
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
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public int getModeType() {
		return modeType;
	}
	public void setModeType(int modeType) {
		this.modeType = modeType;
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	public Long getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}
	@Override
	public String toString() {
		return "LoginResp [result=" + result + ", msg=" + msg + ", yunvaId="
				+ yunvaId + ", modeType=" + modeType + ", isLeader=" + isLeader
				+ ", leaderId=" + leaderId + "]";
	}
	
}
