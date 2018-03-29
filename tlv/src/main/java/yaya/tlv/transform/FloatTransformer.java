package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class FloatTransformer implements Transformer<Float, byte[]> {
    private Convertor<Float, byte[]> convertor;

    public FloatTransformer() {
        try {
            convertor = ConvertorFactory.build(Float.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Float aFloat, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == aFloat) {
            return null;
        } else {
            byte[] data = convertor.encode(aFloat, tlvFieldMeta.getUnsigned());
            if (null != data) {
                return ArrayUtils.addAll(ArrayUtils.addAll(
                        new byte[]{(byte) tlvFieldMeta.getTag()},
                        Bits.putShort((short) data.length)), data);
            } else {
                return null;
            }
        }
    }

    @Override
    public Float decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                        TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
