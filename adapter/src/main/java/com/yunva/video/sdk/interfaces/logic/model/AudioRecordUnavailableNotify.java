package com.yunva.video.sdk.interfaces.logic.model;

public class AudioRecordUnavailableNotify {
	
	private int result = 1;
	private String msg;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "AudioRecordUnavailableNotify [result=" + result + ", msg="
				+ msg + "]";
	}
	
}
