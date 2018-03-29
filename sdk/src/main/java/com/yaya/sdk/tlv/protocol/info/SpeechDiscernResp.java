package com.yaya.sdk.tlv.protocol.info;


public class SpeechDiscernResp {
    private Long result;
    private String msg;
    private String content;//文字内容
    private String url;//网络语音文件路径
    private long voiceDuration; //语音时长(单位:毫秒)
    private String expand; //扩展内容
    private String voiceFilePath;//本地语音文件路径
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(long voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	public String getVoiceFilePath() {
		return voiceFilePath;
	}
	public void setVoiceFilePath(String voiceFilePath) {
		this.voiceFilePath = voiceFilePath;
	}
	@Override
	public String toString() {
		return "SpeechDiscernResp [result=" + result + ", msg=" + msg
				+ ", content=" + content + ", url=" + url + ", voiceDuration="
				+ voiceDuration + ", expand=" + expand + ", voiceFilePath="
				+ voiceFilePath + "]";
	}
}
