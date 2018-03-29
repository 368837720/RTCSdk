package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class ShortTransformer implements Transformer<Short, byte[]> {
    private Convertor<Short, byte[]> convertor;

    public ShortTransformer() {
        try {
            convertor = ConvertorFactory.build(Short.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Short aShort, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == aShort) {
            return null;
        } else {
            byte[] data = convertor.encode(aShort, tlvFieldMeta.getUnsigned());
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
    public Short decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                        TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
