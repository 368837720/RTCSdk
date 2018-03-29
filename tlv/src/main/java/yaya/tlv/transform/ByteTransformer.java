package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class ByteTransformer implements Transformer<Byte, byte[]> {
    private Convertor<Byte, byte[]> convertor;

    public ByteTransformer() {
        try {
            convertor = ConvertorFactory.build(Byte.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Byte aByte, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == aByte) {
            return null;
        } else {
            byte[] data = convertor.encode(aByte, tlvFieldMeta.getUnsigned());
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
    public Byte decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                       TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
