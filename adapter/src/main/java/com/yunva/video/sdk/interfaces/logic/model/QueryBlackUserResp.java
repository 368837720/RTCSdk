package com.yunva.video.sdk.interfaces.logic.model;


public class QueryBlackUserResp {
    private Long result;
    private String msg;
    private Byte position;//座位号
    private Byte state;//视频状态：0关闭，1打开
    private Integer type; //0表示禁掉，1表示取消禁掉,2表示警告中
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
	public Byte getPosition() {
		return position;
	}
	public void setPosition(Byte position) {
		this.position = position;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}
