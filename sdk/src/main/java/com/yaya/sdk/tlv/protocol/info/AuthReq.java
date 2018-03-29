package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 13-8-2.
 */
@TlvMsg(moduleId = 0x2000, msgCode = 0x0003)
public class AuthReq extends TlvSignal {
	@TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
	private Long yunvaId;
	@TlvSignalField(tag = 2)
	private String password;
	@TlvSignalField(tag = 3)
	private String imsi;
	@TlvSignalField(tag = 4)
	private String imei;
	@TlvSignalField(tag = 5)
	private String mac;
	@TlvSignalField(tag = 6)
	private String appId;
	@TlvSignalField(tag = 7)
	private String osType;
	@TlvSignalField(tag = 8)
	private String thirdAccount;
	@TlvSignalField(tag = 9)
	private String appVersion;
	@TlvSignalField(tag = 10)
	private String networkType;//1为wifi,2为3g,3为2g

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

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
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

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	@Override
	public String toString() {
		return "AuthReq [yunvaId=" + yunvaId + ", password=" + password
				+ ", imsi=" + imsi + ", imei=" + imei + ", mac=" + mac
				+ ", appId=" + appId + ", osType=" + osType + ", thirdAccount="
				+ thirdAccount + ", appVersion=" + appVersion
				+ ", networkType=" + networkType + "]";
	}

}
