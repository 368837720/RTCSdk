package com.yaya.sdk.tlv.protocol.info;


import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0x2900, msgCode = 0x0019)
public class GetGmgcUserInfoReq extends TlvSignal {
	
	@TlvSignalField(tag = 1)
    private String tt;
    @TlvSignalField(tag = 2)
    private String appId;
    @TlvSignalField(tag = 3)
    private String imsi;
    @TlvSignalField(tag = 4)
    private String imei;
    @TlvSignalField(tag = 5)
    private String mac;
    @TlvSignalField(tag = 6)
    private String channelId;
    @TlvSignalField(tag = 7)
    private String appVersion;
    @TlvSignalField(tag = 8)
    private String osType;
    @TlvSignalField(tag = 9)
    private String osVersion;
    @TlvSignalField(tag = 10)
    private String sdkAppId;
    @TlvSignalField(tag = 11)
    private String sdkAppVersion;
	
	public String getTt() {
		return tt;
	}
	public void setTt(String tt) {
		this.tt = tt;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getSdkAppId() {
		return sdkAppId;
	}
	public void setSdkAppId(String sdkAppId) {
		this.sdkAppId = sdkAppId;
	}
	public String getSdkAppVersion() {
		return sdkAppVersion;
	}
	public void setSdkAppVersion(String sdkAppVersion) {
		this.sdkAppVersion = sdkAppVersion;
	}
	@Override
	public String toString() {
		return "GetGmgcUserInfoReq [tt=" + tt + ", appId=" + appId + ", imsi="
				+ imsi + ", imei=" + imei + ", mac=" + mac + ", channelId="
				+ channelId + ", appVersion=" + appVersion + ", osType="
				+ osType + ", osVersion=" + osVersion + ", sdkAppId="
				+ sdkAppId + ", sdkAppVersion=" + sdkAppVersion + "]";
	}
	
}
