package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class DoubleTransformer implements Transformer<Double, byte[]> {
    private Convertor<Double, byte[]> convertor;

    public DoubleTransformer() {
        try {
            convertor = ConvertorFactory.build(Double.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Double aDouble, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == aDouble) {
            return null;
        } else {
            byte[] data = convertor.encode(aDouble, tlvFieldMeta.getUnsigned());
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
    public Double decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
