package com.yunva.video.sdk.interfaces.logic.model;

import java.util.List;

public class QueryHistoryMsgResp {
    private Long result;
    private String msg;
    private List<TextMessageNotify> msgs; 
    @Override
	public String toString() {
		return "QueryHistoryMsgResp [result=" + result + ", msg=" + msg + ", msgs=" + msgs + "]";
	}
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
	public List<TextMessageNotify> getMsgs() {
		return msgs;
	}
	public void setMsgs(List<TextMessageNotify> msgs) {
		this.msgs = msgs;
	}
    
}
