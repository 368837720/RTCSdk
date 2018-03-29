package com.yunva.video.sdk.interfaces.logic.model;

import java.util.List;

public class QueryUserListResp {
    private Long result;
    private String msg;
    private List<TroopsUser> users;
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
	public List<TroopsUser> getUsers() {
		return users;
	}
	public void setUsers(List<TroopsUser> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "QueryUserListResp [result=" + result + ", msg=" + msg
				+ ", users=" + users + "]";
	}

}
