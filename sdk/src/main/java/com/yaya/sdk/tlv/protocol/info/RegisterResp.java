package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 13-8-2.
 */
@TlvMsg(moduleId = 0x2000, msgCode = 0x0002)
public class RegisterResp extends TlvSignal {
	@TlvSignalField(tag = 200, unsigned = Unsigned.UINT32)
	private Long result;
	@TlvSignalField(tag = 201)
	private String msg;
	@TlvSignalField(tag = 3, unsigned = Unsigned.UINT32)
	private Long yunvaId;
	@TlvSignalField(tag = 4)
	private String password;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public Long getYunvaId() {
		return yunvaId;
	}

	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("msg:").append(msg);
		sb.append("|result:").append(result);
		sb.append("|yunvaId:").append(yunvaId);
		sb.append("|password:").append(password);
		return sb.toString();
	}
}
