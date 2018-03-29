package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class LongTransformer implements Transformer<Long, byte[]> {
    private Convertor<Long, byte[]> convertor;

    public LongTransformer() {
        try {
            convertor = ConvertorFactory.build(Long.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Long aLong, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == aLong) {
            return null;
        } else {
            byte[] data = convertor.encode(aLong, tlvFieldMeta.getUnsigned());
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
    public Long decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                       TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
