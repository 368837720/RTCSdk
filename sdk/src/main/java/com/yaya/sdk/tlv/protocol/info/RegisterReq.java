package com.yaya.sdk.tlv.protocol.info;


import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 13-8-2.
 */
@TlvMsg(moduleId = 0x2000, msgCode = 0x0001)
public class RegisterReq extends TlvSignal {
	@TlvSignalField(tag = 1)
	private String nickName;
	@TlvSignalField(tag = 2)
	private String password;
	@TlvSignalField(tag = 3)
	private String imsi;
	@TlvSignalField(tag = 4)
	private String imei;
	@TlvSignalField(tag = 5)
	private String phone;
	@TlvSignalField(tag = 6)
	private String mac;
	@TlvSignalField(tag = 7)
	private String factory;
	@TlvSignalField(tag = 8)
	private String model;
	@TlvSignalField(tag = 9)
	private String channelId;
	@TlvSignalField(tag = 10)
	private String appId;
	@TlvSignalField(tag = 11)
	private Byte sex;
	@TlvSignalField(tag = 12)
	private String birthday;
	@TlvSignalField(tag = 13)
	private String star;
	@TlvSignalField(tag = 14)
	private String email;
	@TlvSignalField(tag = 16)
	private String iconUrl;
	@TlvSignalField(tag = 17)
	private String currentProvince;
	@TlvSignalField(tag = 18)
	private String currentCity;
	@TlvSignalField(tag = 19)
	private String birthProvince;
	@TlvSignalField(tag = 20)
	private String birthCity;
	@TlvSignalField(tag = 21)
	private String school;
	@TlvSignalField(tag = 22)
	private String osType;
	@TlvSignalField(tag = 23)
	private String osVersion;
	@TlvSignalField(tag = 24)
	private String cpuType;
	@TlvSignalField(tag = 25)
	private String display;
	@TlvSignalField(tag = 26)
	private String industry;
	@TlvSignalField(tag = 27)
	private String signature;
	@TlvSignalField(tag = 28)
	private String intro;
	@TlvSignalField(tag = 29)
	private String hobby;
	@TlvSignalField(tag = 30)
	private String appVersion;
	@TlvSignalField(tag = 31)
	private String sdkAppId;
	@TlvSignalField(tag = 32)
	private String sdkVersion;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getBirthProvince() {
		return birthProvince;
	}

	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getCurrentProvince() {
		return currentProvince;
	}

	public void setCurrentProvince(String currentProvince) {
		this.currentProvince = currentProvince;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
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

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCpuType() {
		return cpuType;
	}

	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getSdkAppId() {
		return sdkAppId;
	}

	public void setSdkAppId(String sdkAppId) {
		this.sdkAppId = sdkAppId;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RegisterReq{");
		sb.append(", nickName='").append(nickName).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append(", imsi='").append(imsi).append('\'');
		sb.append(", imei='").append(imei).append('\'');
		sb.append(", phone='").append(phone).append('\'');
		sb.append(", mac='").append(mac).append('\'');
		sb.append(", factory='").append(factory).append('\'');
		sb.append(", model='").append(model).append('\'');
		sb.append(", channelId='").append(channelId).append('\'');
		sb.append(", appId='").append(appId).append('\'');
		sb.append(", sex=").append(sex);
		sb.append(", birthday='").append(birthday).append('\'');
		sb.append(", star='").append(star).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append(", iconUrl='").append(iconUrl).append('\'');
		sb.append(", currentProvince='").append(currentProvince).append('\'');
		sb.append(", currentCity='").append(currentCity).append('\'');
		sb.append(", birthProvince='").append(birthProvince).append('\'');
		sb.append(", birthCity='").append(birthCity).append('\'');
		sb.append(", school='").append(school).append('\'');
		sb.append(", osType='").append(osType).append('\'');
		sb.append(", osVersion='").append(osVersion).append('\'');
		sb.append(", cpuType='").append(cpuType).append('\'');
		sb.append(", display='").append(display).append('\'');
		sb.append(", industry='").append(industry).append('\'');
		sb.append(", signature='").append(signature).append('\'');
		sb.append(", intro='").append(intro).append('\'');
		sb.append(", hobby='").append(hobby).append('\'');
		sb.append(", appVersion='").append(appVersion).append('\'');
		sb.append(", sdkAppId='").append(sdkAppId).append('\'');
		sb.append(", sdkVersion='").append(sdkVersion).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
