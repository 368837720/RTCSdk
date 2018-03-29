package com.yaya.sdk.tlv;

import yaya.tlv.TlvStore;
import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.header.TlvAccessHeader;

import yaya.tlv.signal.TlvSignal;
import yaya.tlv.util.TlvCodecUtil;

/**
 * Created by wy on 14-3-18.
 */
public class TlvUtil {

	public static int getModuleId(TlvSignal signal) {
		TlvMsg tlvMsg = signal.getClass().getAnnotation(TlvMsg.class);
		return tlvMsg.moduleId();
	}

	public static int getMsgCode(TlvSignal signal) {
		TlvMsg tlvMsg = signal.getClass().getAnnotation(TlvMsg.class);
		return tlvMsg.msgCode();
	}

	public static int getModuleId(Class<? extends TlvSignal> clazz) {
		TlvMsg tlvMsg = clazz.getAnnotation(TlvMsg.class);
		return tlvMsg.moduleId();
	}

	public static int getMsgCode(Class<? extends TlvSignal> clazz) {
		TlvMsg tlvMsg = clazz.getAnnotation(TlvMsg.class);
		return tlvMsg.msgCode();
	}

	public static TlvAccessHeader buildHeader(long seqNum, Integer moduleId,
											  Integer msgCode) {
		TlvAccessHeader header = new TlvAccessHeader();
		header.setCompresed((byte) 0);
		header.setEncrypted((byte) 0);
		header.setEsbAddr(moduleId);
		header.setMessageId(seqNum);
		header.setTag((byte) 0);
		header.setVersion((byte) 0);
		header.setMsgCode(msgCode);
		return header;
	}

	public static TlvAccessHeader buildHeader(TlvSignal signal, long seqNum) {
		return buildHeader(seqNum, getModuleId(signal), getMsgCode(signal));
	}

	public static byte[] encodeTlvSignalBody(TlvSignal signal, TlvStore tlvStore)
			throws Exception {
		return TlvCodecUtil.encodeTlvSignal(signal,
				tlvStore.getTlvFieldMeta(signal.getClass()), tlvStore);
	}

	public static TlvSignal decodeTlvSignal(Integer moduleId, Integer msgCode,
											byte[] value, TlvStore tlvStore) throws Exception {
		return TlvCodecUtil.decodeTlvSignal(moduleId, msgCode, value, tlvStore);
	}
}
