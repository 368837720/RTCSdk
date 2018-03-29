package yaya.tlv.transform;

import android.util.SparseArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import yaya.tlv.TlvStore;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.signal.TlvSignal;
import yaya.tlv.util.Bits;
import yaya.tlv.util.TlvCodecUtil;

public class TlvSignalTransformer implements Transformer<TlvSignal, byte[]> {
    @Override
    public byte[] encode(TlvSignal tlvSignal, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) throws Exception {
        if (null == tlvSignal) {
            return null;
        } else {
            if (List.class.isAssignableFrom(tlvFieldMeta.getField().getType())) {
                Type fc = tlvFieldMeta.getField().getGenericType();
                ParameterizedType pt = (ParameterizedType) fc;
                Class genericClass = (Class) pt.getActualTypeArguments()[0]; // generic
                // type
                SparseArray<TlvFieldMeta> tlvFieldMetaMap = tlvStore
                        .getTlvFieldMeta(genericClass);

                byte[] data = TlvCodecUtil.encodeTlvSignal(tlvSignal,
                        tlvFieldMetaMap, tlvStore);
                if (null != data) {
                    return ArrayUtils.addAll(ArrayUtils.addAll(
                            new byte[]{(byte) tlvFieldMeta.getTag()},
                            Bits.putShort((short) data.length)), data);
                } else {
                    return null;
                }
            } else {
                SparseArray<TlvFieldMeta> tlvFieldMetaMap = tlvStore
                        .getTlvFieldMeta(tlvFieldMeta.getField().getType());

                byte[] data = TlvCodecUtil.encodeTlvSignal(tlvSignal,
                        tlvFieldMetaMap, tlvStore);
                if (null != data) {
                    return ArrayUtils.addAll(ArrayUtils.addAll(
                            new byte[]{(byte) tlvFieldMeta.getTag()},
                            Bits.putShort((short) data.length)), data);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public TlvSignal decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                            TlvStore tlvStore) throws Exception {
        if (null == bytes) {
            return null;
        } else {
            if (List.class.isAssignableFrom(tlvFieldMeta.getField().getType())) {
                Type fc = tlvFieldMeta.getField().getGenericType();
                ParameterizedType pt = (ParameterizedType) fc;
                Class genericClass = (Class) pt.getActualTypeArguments()[0]; // generic
                // type
                Class<? extends TlvSignal> type = (Class<? extends TlvSignal>) genericClass;
                return TlvCodecUtil.decodeTlvSignal(bytes, type,
                        tlvStore.getTlvFieldMeta(genericClass), tlvStore);
            } else {
                Class<? extends TlvSignal> type = (Class<? extends TlvSignal>) tlvFieldMeta
                        .getField().getType();
                return TlvCodecUtil.decodeTlvSignal(bytes, type, tlvStore
                                .getTlvFieldMeta(tlvFieldMeta.getField().getType()),
                        tlvStore);
            }
        }
    }
}
