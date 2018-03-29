package com.yunva.video.sdk.interfaces.logic.model;


/**
 * Created by wy on 13-8-2.
 */
public class AuthResp {
	private Long result;
	private String msg;
	private Long yunvaId;
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
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	@Override
	public String toString() {
		return "AuthResp [result=" + result + ", msg=" + msg + ", yunvaId="
				+ yunvaId + "]";
	}
	
}
