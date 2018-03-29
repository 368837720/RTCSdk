package com.yunva.video.sdk.interfaces.logic.model;


public class ModeSettingResp {
    private Long result;
    private String msg;
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
	@Override
	public String toString() {
		return "ModeSettingResp [result=" + result + ", msg=" + msg + "]";
	}

}
