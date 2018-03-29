package com.yunva.video.sdk.interfaces.logic.model;


public class TroopsUser {
    private Long yunvaId;
    private Byte position;
    private Byte state;

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
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "TroopsUser [yunvaId=" + yunvaId + ", position=" + position
				+ ", state=" + state + "]";
	}
	
}

