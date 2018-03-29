package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB300, msgCode = 0x0002)
public class GetTroopsInfoResp extends TlvSignal {
	@TlvSignalField(tag = 200, unsigned = Unsigned.UINT32)
	private Long result;
	@TlvSignalField(tag = 201)
	private String msg;
	@TlvSignalField(tag = 1)
	private String host;
	@TlvSignalField(tag = 2)
	private Integer port;
	@TlvSignalField(tag = 3)
	private String troopsId;
	@TlvSignalField(tag = 4)
	private String token;
	@TlvSignalField(tag = 5)
	private String videoHost;
	@TlvSignalField(tag = 6)
	private Integer videoPort;

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

	public String getTroopsId() {
		return troopsId;
	}

	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVideoHost() {
		return videoHost;
	}

	public void setVideoHost(String videoHost) {
		this.videoHost = videoHost;
	}

	public Integer getVideoPort() {
		return videoPort;
	}

	public void setVideoPort(Integer videoPort) {
		this.videoPort = videoPort;
	}

	@Override
	public String toString() {
		return "GetTroopsInfoResp [result=" + result + ", msg=" + msg
				+ ", host=" + host + ", port=" + port + ", troopsId="
				+ troopsId + ", token=" + token + ", videoHost=" + videoHost
				+ ", videoPort=" + videoPort + "]";
	}

}