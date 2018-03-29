package com.yunva.video.sdk.interfaces.logic.model;


/**
 * Created by wy on 14-3-14.
 */
public class TextMessageResp {
    private Long result;
    private String msg;
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

    public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	@Override
	public String toString() {
		return "TextMessageResp [result=" + result + ", msg=" + msg
				+ ", expand=" + expand + "]";
	}

}
