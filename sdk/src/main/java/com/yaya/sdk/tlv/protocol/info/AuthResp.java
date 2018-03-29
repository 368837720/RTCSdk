package com.yaya.sdk.tlv.protocol.info;


import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 13-8-2.
 */
@TlvMsg(moduleId = 0x2000, msgCode = 0x0004)
public class AuthResp extends TlvSignal {
	@TlvSignalField(tag = 200, unsigned = Unsigned.UINT32)
	private Long result;
	@TlvSignalField(tag = 201)
	private String msg;
	@TlvSignalField(tag = 3, unsigned = Unsigned.UINT32)
	private Long yunvaId;
	@TlvSignalField(tag = 4)
	private String phone;
	@TlvSignalField(tag = 5)
	private String nickName;
	@TlvSignalField(tag = 6)
	private Byte sex;
	@TlvSignalField(tag = 7)
	private String birthday;
	@TlvSignalField(tag = 8)
	private String email;
	@TlvSignalField(tag = 9)
	private String accountType;
	@TlvSignalField(tag = 10)
	private String iconUrl;
	@TlvSignalField(tag = 11)
	private String signature;
	@TlvSignalField(tag = 12)
	private String currentProvince;
	@TlvSignalField(tag = 13)
	private String currentCity;
	@TlvSignalField(tag = 14)
	private String birthProvince;
	@TlvSignalField(tag = 15)
	private String birthCity;
	@TlvSignalField(tag = 16)
	private String school;
	@TlvSignalField(tag = 17)
	private String industry;
	@TlvSignalField(tag = 18)
	private String intro;
	@TlvSignalField(tag = 19)
	private String star;
	@TlvSignalField(tag = 20)
	private String hobby;
	@TlvSignalField(tag = 21)
	private String qq;
	@TlvSignalField(tag = 22)
	private String weixin;
	@TlvSignalField(tag = 23)
	private String weibo;
	@TlvSignalField(tag = 202)
	private String token;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public Long getYunvaId() {
		return yunvaId;
	}

	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "AuthResp [result=" + result + ", msg=" + msg + ", yunvaId="
				+ yunvaId + ", phone=" + phone + ", nickName=" + nickName
				+ ", sex=" + sex + ", birthday=" + birthday + ", email="
				+ email + ", accountType=" + accountType + ", iconUrl="
				+ iconUrl + ", signature=" + signature + ", currentProvince="
				+ currentProvince + ", currentCity=" + currentCity
				+ ", birthProvince=" + birthProvince + ", birthCity="
				+ birthCity + ", school=" + school + ", industry=" + industry
				+ ", intro=" + intro + ", star=" + star + ", hobby=" + hobby
				+ ", qq=" + qq + ", weixin=" + weixin + ", weibo=" + weibo
				+ ", token=" + token + "]";
	}

}
