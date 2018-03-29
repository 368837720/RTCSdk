package com.yunva.video.sdk.interfaces.logic.model;


public class InviteUserVideoNotify {
    private Long yunvaId;
    private String nickname;
    private String troopsId;
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
		return "InviteUserVideoNotify [yunvaId=" + yunvaId + ", nickname="
				+ nickname + ", troopsId=" + troopsId + ", ext=" + ext
				+ ", position=" + position + "]";
	}
	
}
