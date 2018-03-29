package com.yaya.sdk.tlv.protocol;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

@TlvMsg(moduleId = 0xB400, msgCode = 0x0003)
public class LoginReq extends TlvSignal {
    @TlvSignalField(tag = 1, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 2)
    private String token;
    @TlvSignalField(tag = 3)
    private String troopsId;
    @TlvSignalField(tag = 4)
    private String appId;
    @TlvSignalField(tag = 5)
    private String sdkAppId;
    @TlvSignalField(tag = 6)
    private String sdkAppVersion;
    @TlvSignalField(tag = 7)
    private String remark;
    @TlvSignalField(tag = 8)
    private String seq;
    @TlvSignalField(tag = 10)
    private String osType;
    @TlvSignalField(tag = 11)
    private String osVersion;

	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
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
	@Override
	public String toString() {
		return "LoginReq [yunvaId=" + yunvaId + ", token=" + token
				+ ", troopsId=" + troopsId + ", appId=" + appId + ", sdkAppId="
				+ sdkAppId + ", sdkAppVersion=" + sdkAppVersion + ", remark="
				+ remark + ", seq=" + seq + ", osType=" + osType
				+ ", osVersion=" + osVersion + "]";
	}
	
}
