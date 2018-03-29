package com.yunva.video.sdk.interfaces.logic.model;


public class RealTimeVoiceMessageNotify {
	
	private String troopsId;
    private Long yunvaId;
    private String expand;
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	@Override
	public String toString() {
		return "RealTimeVoiceMessageNotify [troopsId=" + troopsId
				+ ", yunvaId=" + yunvaId + ", expand=" + expand + "]";
	}
	
}
