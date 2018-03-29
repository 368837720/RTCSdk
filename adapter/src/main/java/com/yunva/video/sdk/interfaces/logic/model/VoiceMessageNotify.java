package com.yunva.video.sdk.interfaces.logic.model;

/**
 * Created by wy on 14-3-16.
 */


public class VoiceMessageNotify {
    private String troopsId;
    private Long yunvaId;
    private String voiceUrl;
    private long voiceTime;
    private Long time;
    private String expand;
    private String text;
    
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
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
	public String getVoiceUrl() {
		return voiceUrl;
	}
	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	public long getVoiceTime() {
		return voiceTime;
	}
	public void setVoiceTime(long voiceTime) {
		this.voiceTime = voiceTime;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	@Override
	public String toString() {
		return "VoiceMessageNotify [troopsId=" + troopsId + ", yunvaId=" + yunvaId + ", voiceUrl=" + voiceUrl + ", voiceTime=" + voiceTime + ", time=" + time + ", expand=" + expand + ", text=" + text + "]";
	}
	
}
