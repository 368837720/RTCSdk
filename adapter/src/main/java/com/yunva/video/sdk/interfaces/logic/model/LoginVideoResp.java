package com.yunva.video.sdk.interfaces.logic.model;

import java.util.List;

public class LoginVideoResp {
    private Long result;
    private String msg;
    private List<TroopsUser> users;
    private Integer type; //0表示禁掉，1表示取消禁掉

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "LoginVideoResp [result=" + result + ", msg=" + msg + ", users="
				+ users + ", type=" + type + "]";
	}

}
