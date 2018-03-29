package yaya.tlv.util;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.ShortConvertor;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.meta.TlvHeaderFieldMeta;
import yaya.tlv.signal.TlvSignal;

public class TlvCodecUtil {
    private static final String TAG = "TlvCodecUtil";
    private static final int TAG_LENGTH = 1;
    private static final int LENGTH_LENGTH = 2;
    private static ShortConvertor lengthConvertor = new ShortConvertor();
    private static ShortConvertor tagConvertor = new ShortConvertor();

    public static <T extends TlvAccessHeader> T decodeHeader(Class<T> type,
                                                             byte[] data, TlvStore tlvStore) throws IllegalAccessException,
            InstantiationException {
        Iterator<TlvHeaderFieldMeta> iterator = tlvStore.getTlvHeaderFieldMeta(
                type).iterator();
        int index = 0;
        Object bean = type.newInstance();
        while (iterator.hasNext()) {
            TlvHeaderFieldMeta tlvHeaderFieldMeta = iterator.next();
            int byteLength = Unsigned.length(tlvHeaderFieldMeta.getUnsigned(),
                    tlvHeaderFieldMeta.getField().getType());
            byte[] fieldData = ArrayUtils.subarray(data, index, index
                    + byteLength);
            tlvHeaderFieldMeta.getField().setAccessible(true);
            tlvHeaderFieldMeta.getField().set(bean,
                    tlvHeaderFieldMeta.getConvertor().decode(fieldData));
            // update index
            index = index + byteLength;
        }
        return (T) bean;
    }

    public static TlvSignal decodeTlvSignal(TlvAccessHeader tlvHeader,
                                            byte[] data, TlvStore tlvStore) throws Exception {
        // T-A消息
        Class type = tlvStore.getTypeMeta(tlvHeader.getEsbAddr(),
                tlvHeader.getMsgCode());
        if (null != type) {
            return decodeTlvSignal(data, type, tlvStore.getTlvFieldMeta(type),
                    tlvStore);
        } else {
            Log.w(TAG, "msgCode:" + tlvHeader.getMsgCode()
                    + " isn't implement.");
            return null;
        }
    }

    public static TlvSignal decodeTlvSignal(Integer moduleId, Integer msgCode,
                                            byte[] data, TlvStore tlvStore) throws Exception {
        // T-A消息
        Class type = tlvStore.getTypeMeta(moduleId, msgCode);
        if (null != type) {
            return decodeTlvSignal(data, type, tlvStore.getTlvFieldMeta(type),
                    tlvStore);
        } else {
            Log.w(TAG, "msgCode:" + msgCode + " isn't implement.");
            // throw new IllegalAccessException("illegal msgCode...");
            return null;
        }
    }

    public static <T extends TlvSignal> T decodeTlvSignal(byte[] data,
                                                          Class<T> type, SparseArray<TlvFieldMeta> fieldMeta,
                                                          TlvStore tlvStore) throws Exception {
        Object bean = type.newInstance();
        if (null == data || data.length < (TAG_LENGTH + LENGTH_LENGTH)) {
            return (T) bean;
        }

        if (null != fieldMeta && fieldMeta.size() > 0) {
            for (int index = 0; index < data.length; ) {
                int tag = tagConvertor.decode(ArrayUtils.subarray(data, index,
                        index + TAG_LENGTH));
                index = index + TAG_LENGTH;
                int length = lengthConvertor.decode(ArrayUtils.subarray(data,
                        index, index + LENGTH_LENGTH));
                index = index + LENGTH_LENGTH;

                if (length > 0) {
                    TlvFieldMeta tlvFieldMeta = fieldMeta.get(tag);
                    if (null != tlvFieldMeta) {
                        Object value = tlvFieldMeta.getTransformer()
                                .decode(ArrayUtils.subarray(data, index, index
                                        + length), tlvFieldMeta, tlvStore);
                        index = index + length;
                        if (null != value) {
                            tlvFieldMeta.getField().setAccessible(true);
                            // 特殊处理一下List
                            if (List.class.isAssignableFrom(tlvFieldMeta
                                    .getField().getType())) {
                                List list = (List) tlvFieldMeta.getField().get(
                                        bean);
                                if (null == list) {
                                    list = new ArrayList();
                                    list.add(value);
                                    tlvFieldMeta.getField().set(bean, list);
                                } else {
                                    list.add(value);
                                    tlvFieldMeta.getField().set(bean, list);
                                }
                            } else {
                                tlvFieldMeta.getField().set(bean, value);
                            }
                        }
                    } else {
                        // 此属性现在不支持,跳过
                        index = index + length;
                    }
                }
            }
        }

        return (T) bean;
    }

    public static byte[] encodeTlvSignal(Object object,
                                         SparseArray<TlvFieldMeta> fieldMeta, TlvStore tlvStore)
            throws Exception {
        if (null == fieldMeta || fieldMeta.size() == 0) {
            return null;
        } else {
            byte[] data = null;
            for (int i = 0, size = fieldMeta.size(); i < size; i++) {
                TlvFieldMeta tlvFieldMeta = fieldMeta.valueAt(i);
                tlvFieldMeta.getField().setAccessible(true);
                Object fieldValue = tlvFieldMeta.getField().get(object);

                if (null != fieldValue) {
                    // 特殊处理一下List
                    if (List.class.isAssignableFrom(tlvFieldMeta.getField()
                            .getType())) {
                        List list = (List) fieldValue;
                        for (Object o : list) {
                            byte[] fieldData = (byte[]) tlvFieldMeta
                                    .getTransformer().encode(o, tlvFieldMeta,
                                            tlvStore);
                            if (null != fieldData) {
                                data = ArrayUtils.addAll(data, fieldData);
                            }
                        }
                    } else {
                        byte[] fieldData = (byte[]) tlvFieldMeta
                                .getTransformer().encode(fieldValue,
                                        tlvFieldMeta, tlvStore);
                        if (null != fieldData) {
                            data = ArrayUtils.addAll(data, fieldData);
                        }
                    }
                }
            }
            return data;
        }
    }

    public static byte[] encodeSignal(TlvSignal tlvSignal, TlvStore tlvStore)
            throws Exception {
        byte[] signalData = TlvCodecUtil.encodeTlvSignal(tlvSignal,
                tlvStore.getTlvFieldMeta(tlvSignal.getClass()), tlvStore);
        int signalDataLength = (null == signalData ? 0 : signalData.length);
        tlvSignal.getHeader().setLength(
                TlvAccessHeader.HEADER_LENGTH + signalDataLength);
        byte[] tlvHeaderData = encodeTlvHeader(tlvSignal.getHeader(), tlvStore);

        return ArrayUtils.addAll(tlvHeaderData, signalData);
    }

    public static byte[] encodeTlvHeader(TlvAccessHeader header,
                                         TlvStore tlvStore) throws IllegalAccessException {
        byte[] headerData = null;
        Iterator<TlvHeaderFieldMeta> iterator = tlvStore.getTlvHeaderFieldMeta(
                header.getClass()).iterator();
        while (iterator.hasNext()) {
            TlvHeaderFieldMeta tlvHeaderFieldMeta = iterator.next();
            tlvHeaderFieldMeta.getField().setAccessible(true);
            Object fieldValue = tlvHeaderFieldMeta.getField().get(header);
            byte[] fieldData = (byte[]) tlvHeaderFieldMeta.getConvertor()
                    .encode(fieldValue, tlvHeaderFieldMeta.getUnsigned());

            headerData = ArrayUtils.addAll(headerData, fieldData);
        }
        return headerData;
    }
}
