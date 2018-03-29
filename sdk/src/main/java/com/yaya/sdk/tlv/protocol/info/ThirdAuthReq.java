package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0x2000, msgCode = 0x0051)
public class ThirdAuthReq extends TlvSignal {
	
	@TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
	private Long yunvaId;
	@TlvSignalField(tag = 2)
	private String t;
	@TlvSignalField(tag = 3)
	private String thirdId;
	@TlvSignalField(tag = 4)
	private String thirdUserName;
	@TlvSignalField(tag = 5)
	private String imsi;
	@TlvSignalField(tag = 6)
	private String imei;
	@TlvSignalField(tag = 7)
	private String mac;
	@TlvSignalField(tag = 8)
	private String appId;
	@TlvSignalField(tag = 9)
	private String osType;
	@TlvSignalField(tag = 10)
	private String networkType;
	
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getThirdId() {
		return thirdId;
	}
	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
	public String getThirdUserName() {
		return thirdUserName;
	}
	public void setThirdUserName(String thirdUserName) {
		this.thirdUserName = thirdUserName;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	@Override
	public String toString() {
		return "ThirdAuthReq [yunvaId=" + yunvaId + ", t=" + t + ", thirdId="
				+ thirdId + ", thirdUserName=" + thirdUserName + ", imsi="
				+ imsi + ", imei=" + imei + ", mac=" + mac + ", appId=" + appId
				+ ", osType=" + osType + ", networkType=" + networkType + "]";
	}
	
}