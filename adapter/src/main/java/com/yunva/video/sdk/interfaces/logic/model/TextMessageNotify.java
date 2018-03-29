package com.yunva.video.sdk.interfaces.logic.model;

/**
 * Created by wy on 14-3-16.
 */


public class TextMessageNotify {
    private String troopsId;
    private Long yunvaId;
    private String text;
    private String richText;
    private Long time;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRichText() {
		return richText;
	}
	public void setRichText(String richText) {
		this.richText = richText;
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
		return "TextMessageNotify [troopsId=" + troopsId + ", yunvaId="
				+ yunvaId + ", text=" + text + ", richText=" + richText
				+ ", time=" + time + ", expand=" + expand + "]";
	}

}
