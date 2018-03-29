package com.yunva.video.sdk.interfaces.logic.model;



public class UserStateNotify {
	private Long yunvaId;
    private String troopsId;
    private Byte type;   //1是send,2是receiver
    private String state; //00表示全空，右边第一位是文字，左边是实时语音
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
