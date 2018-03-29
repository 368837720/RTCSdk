package com.yaya.sdk.tlv.protocol.info;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB300,msgCode = 0x0001)
public class GetTroopsInfoReq extends TlvSignal {
	@TlvSignalField(tag = 1)
    private String appId;
    @TlvSignalField(tag = 2)
    private String seq;
    @TlvSignalField(tag = 3)
    private String sdkAppId;
    @TlvSignalField(tag = 4)
    private String sdkAppVersion;
    @TlvSignalField(tag = 5)
    private String imsi;
    @TlvSignalField(tag = 6)
    private String imei;
    @TlvSignalField(tag = 7)
    private String mac;
    @TlvSignalField(tag = 8)
    private String osType;
    @TlvSignalField(tag = 9)
    private String osVersion;
    @TlvSignalField(tag = 10)
    private String hasVideo = "0";  //1表示有

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
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
	public String getHasVideo() {
		return hasVideo;
	}
	public void setHasVideo(String hasVideo) {
		this.hasVideo = hasVideo;
	}
	@Override
	public String toString() {
		return "GetTroopsInfoReq [appId=" + appId + ", seq=" + seq
				+ ", sdkAppId=" + sdkAppId + ", sdkAppVersion=" + sdkAppVersion
				+ ", imsi=" + imsi + ", imei=" + imei + ", mac=" + mac
				+ ", osType=" + osType + ", osVersion=" + osVersion
				+ ", hasVideo=" + hasVideo + "]";
	}
	
}