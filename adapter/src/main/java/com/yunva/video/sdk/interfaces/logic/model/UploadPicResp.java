package com.yunva.video.sdk.interfaces.logic.model;


public class UploadPicResp {
	private Long result;		//结果码
    private String msg;			//错误Msg
	private String picUrl;			//图片url
	private String smallPicUrl;		//小图url
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getSmallPicUrl() {
		return smallPicUrl;
	}
	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}
	@Override
	public String toString() {
		return "UploadPicResp [result=" + result + ", msg=" + msg + ", picUrl="
				+ picUrl + ", smallPicUrl=" + smallPicUrl + "]";
	}
	
}
