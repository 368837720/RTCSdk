package com.yaya.sdk.tlv.protocol.message;

/**
 * Created by wy on 14-3-16.
 */
public class MixVoiceMessage {
    private Long yunvaId;
    private byte[] msg;
    private int position;
    public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public byte[] getMsg() {
		return msg;
	}
	public void setMsg(byte[] msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "MixVoiceMessage0 [yunvaId=" + yunvaId + ", msg="
				+ msg + "]";
	}

}
