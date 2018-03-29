package com.yunva.video.sdk.interfaces.logic.model;


public class UploadVoiceResp {
	private Long result;		//结果码
    private String msg;			//错误Msg
	private String voiceUrl;			//语音url
	private String expand;
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
	public String getVoiceUrl() {
		return voiceUrl;
	}
	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	@Override
	public String toString() {
		return "UploadVoiceResp [result=" + result + ", msg=" + msg
				+ ", voiceUrl=" + voiceUrl + ", expand=" + expand + "]";
	}
	
}
