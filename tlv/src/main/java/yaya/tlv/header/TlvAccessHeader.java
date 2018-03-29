package yaya.tlv.header;

import yaya.tlv.Tlvable;
import yaya.tlv.annotation.TlvHeaderField;
import yaya.tlv.convertor.unsigned.Unsigned;

/**
 * Created by wy on 13-7-31.
 */
public class TlvAccessHeader implements Tlvable {
    public static final int HEADER_LENGTH = 14;
    @TlvHeaderField(index = 1)
    private Byte version; // 包头版本号
    @TlvHeaderField(index = 2)
    private Byte encrypted; // 是否已加密
    @TlvHeaderField(index = 3)
    private Byte compresed; // 是否已压缩
    @TlvHeaderField(index = 4)
    private Byte tag; // 保留,目前默认为0
    @TlvHeaderField(index = 5, unsigned = Unsigned.UINT32)
    private Long messageId;
    @TlvHeaderField(index = 6, unsigned = Unsigned.UINT16)
    private Integer length; // 包体长度, 整个包的长度
    @TlvHeaderField(index = 7, unsigned = Unsigned.UINT16)
    private Integer esbAddr;// 地址
    @TlvHeaderField(index = 8, unsigned = Unsigned.UINT16)
    private Integer msgCode; // 命令ID

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(Integer msgCode) {
        this.msgCode = msgCode;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setEsbAddr(Integer esbAddr) {
        this.esbAddr = esbAddr;
    }

    public Byte getTag() {
        return tag;
    }

    public void setTag(Byte tag) {
        this.tag = tag;
    }

    public Byte getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Byte encrypted) {
        this.encrypted = encrypted;
    }

    public Byte getCompresed() {
        return compresed;
    }

    public void setCompresed(Byte compresed) {
        this.compresed = compresed;
    }

    public Integer getEsbAddr() {
        return esbAddr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TlvAccessHeader{");
        sb.append("compresed=").append(compresed);
        sb.append(", version=").append(version);
        sb.append(", encrypted=").append(encrypted);
        sb.append(", tag=").append(tag);
        sb.append(", messageId=").append(messageId);
        sb.append(", length=").append(length);
        sb.append(", esbAddr=").append(esbAddr);
        sb.append(", msgCode=").append(msgCode);
        sb.append('}');
        return sb.toString();
    }
}
