package com.yunva.video.sdk.interfaces.logic.model;


public class InviteUserVideoRespNotify {
    private Long yunvaId;
    private String nickname;
    private Long inviter;
    private String troopsId;
    private Byte state;    //0表示拒绝，1表示同意
    private String remark; //拒绝理由
    private String ext;
    private Byte position;
    private Byte inviterPosition;
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
	public Long getInviter() {
		return inviter;
	}
	public void setInviter(Long inviter) {
		this.inviter = inviter;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Byte getInviterPosition() {
		return inviterPosition;
	}
	public void setInviterPosition(Byte inviterPosition) {
		this.inviterPosition = inviterPosition;
	}
	
}
