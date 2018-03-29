package com.yunva.video.sdk.interfaces.logic.model;


public class VideoStateNotify{
    private Long yunvaId;
    private String nickname;
    private String troopsId;
    private Byte state; //0关闭，1打开
    private String ext;
    private Byte position;
    
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
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public Byte getPosition() {
		return position;
	}
	public void setPosition(Byte position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "VideoStateNotify [yunvaId=" + yunvaId + ", nickname="
				+ nickname + ", troopsId=" + troopsId + ", state=" + state
				+ ", ext=" + ext + ", position=" + position + "]";
	}
	
}
