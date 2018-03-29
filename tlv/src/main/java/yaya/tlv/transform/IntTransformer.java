package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class IntTransformer implements Transformer<Integer, byte[]> {
    private Convertor<Integer, byte[]> convertor;

    public IntTransformer() {
        try {
            convertor = ConvertorFactory.build(Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Integer integer, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == integer) {
            return null;
        } else {
            byte[] data = convertor.encode(integer, tlvFieldMeta.getUnsigned());
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
    public Integer decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                          TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
