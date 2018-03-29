package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0x2900, msgCode = 0x0020)
public class GetGmgcUserInfoResp extends TlvSignal {
	
	@TlvSignalField(tag = 200, unsigned = Unsigned.UINT32)
    private Long result = 0L;
    @TlvSignalField(tag = 201)
    private String msg;
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 2)
    private String password;
    @TlvSignalField(tag = 3)
    private String nickname;
    @TlvSignalField(tag = 4)
    private String thirdUserId;
    @TlvSignalField(tag = 5)
    private String thirdUserName;
    @TlvSignalField(tag = 6)
    private String t;
    @TlvSignalField(tag = 7)
    private Integer reqType;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getThirdUserId() {
		return thirdUserId;
	}
	public void setThirdUserId(String thirdUserId) {
		this.thirdUserId = thirdUserId;
	}
	public String getThirdUserName() {
		return thirdUserName;
	}
	public void setThirdUserName(String thirdUserName) {
		this.thirdUserName = thirdUserName;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public Integer getReqType() {
		return reqType;
	}
	public void setReqType(Integer reqType) {
		this.reqType = reqType;
	}
	@Override
	public String toString() {
		return "GetGmgcUserInfoResp [result=" + result + ", msg=" + msg
				+ ", yunvaId=" + yunvaId + ", password=" + password
				+ ", nickname=" + nickname + ", thirdUserId=" + thirdUserId
				+ ", thirdUserName=" + thirdUserName + ", t=" + t
				+ ", reqType=" + reqType + "]";
	} 

}
