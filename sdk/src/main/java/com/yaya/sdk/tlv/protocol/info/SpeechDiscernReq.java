package com.yaya.sdk.tlv.protocol.info;


/**
 * 语言识别
 */
public class SpeechDiscernReq {
    private String appId;
    private Long yunvaId;
    private String textType = "0";//文本类型,简体字=0/繁体字=1
    private String format;      //语言格式,pcm/wav/opus/speex/amr/x-flac
    private int rate;           //采样率,支持8000或者16000
    private String channel;		//声道数,仅支持单声道,请填写 1
    private String cuid;		//用户 id,推荐使用 mac imei等类似参数
    private Long len = 0L;			//原始语音长度,单位字节
    private String lan;			//语种选择,中文=zh粤语=ct英文=en,不区分大小写,默认中文
    private String speech;		//真 实 的 语 音 数 据 ,需 要 进 行base64 编码
    private String url;			//语音下载地址
    private String expires = "4";//语种文件有效期,0=永久 1=一年 2=六个月 3=3个月 4=1个月 5=两周 6=一周 7=三天 8=一天 9=六小时
    
    private long voiceDuration; //语音时长(单位:毫秒)
    private String expand; //扩展内容
    private String voiceFilePath;//本地语音文件路径
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getTextType() {
		return textType;
	}
	public void setTextType(String textType) {
		this.textType = textType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCuid() {
		return cuid;
	}
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	public Long getLen() {
		return len;
	}
	public void setLen(Long len) {
		this.len = len;
	}
	public String getLan() {
		return lan;
	}
	public void setLan(String lan) {
		this.lan = lan;
	}
	public String getSpeech() {
		return speech;
	}
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public long getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(long voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	public String getVoiceFilePath() {
		return voiceFilePath;
	}
	public void setVoiceFilePath(String voiceFilePath) {
		this.voiceFilePath = voiceFilePath;
	}
	@Override
	public String toString() {
		return "SpeechDiscernReq [appId=" + appId + ", yunvaId=" + yunvaId
				+ ", textType=" + textType + ", format=" + format + ", rate="
				+ rate + ", channel=" + channel + ", cuid=" + cuid + ", len="
				+ len + ", lan=" + lan + ", speech=" + "太长了不打印了..." + ", url=" + url
				+ ", expires=" + expires + ", voiceDuration=" + voiceDuration
				+ ", expand=" + expand + ", voiceFilePath=" + voiceFilePath
				+ "]";
	}
    
}
