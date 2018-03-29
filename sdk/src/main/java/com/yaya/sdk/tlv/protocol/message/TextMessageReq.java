package com.yaya.sdk.tlv.protocol.message;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by wy on 14-3-13.
 */
@TlvMsg(moduleId = 0xB400, msgCode = 0x1005)
public class TextMessageReq extends TlvSignal {
    @TlvSignalField(tag = 1)
    private String troopsId;
    @TlvSignalField(tag = 2, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 3)
    private String text;
    @TlvSignalField(tag = 4)
    private String richText;
    @TlvSignalField(tag = 5)
    private String expand;
	public String getTroopsId() {
		return troopsId;
	}
	public void setTroopsId(String troopsId) {
		this.troopsId = troopsId;
	}
	public Long getYunvaId() {
		return yunvaId;
	}
	public void setYunvaId(Long yunvaId) {
		this.yunvaId = yunvaId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRichText() {
		return richText;
	}
	public void setRichText(String richText) {
		this.richText = richText;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	@Override
	public String toString() {
		return "TextMessageReq [troopsId=" + troopsId + ", yunvaId=" + yunvaId
				+ ", text=" + text + ", richText=" + richText + ", expand="
				+ expand + "]";
	}

}
