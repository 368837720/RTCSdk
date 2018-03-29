package com.yunva.video.sdk.interfaces.logic.model;


public class UserLoginNotify {
    private Long yunvaId;
    private Byte position;
    private Byte type; //0表示离开，1表示进来
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public Byte getPosition() {
		return position;
	}
	public void setPosition(Byte position) {
		this.position = position;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "UserLoginNotify [yunvaId=" + yunvaId + ", position=" + position
				+ ", type=" + type + "]";
	}

}
