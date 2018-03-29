package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class StringTransformer implements Transformer<String, byte[]> {
    private Convertor<String, byte[]> convertor;

    public StringTransformer() {
        try {
            convertor = ConvertorFactory.build(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(String s, TlvFieldMeta tlvFieldMeta, TlvStore tlvStore)
            throws Exception {
        byte[] data = convertor.encode(s, tlvFieldMeta.getUnsigned());
        if (null != data) {
            return ArrayUtils.addAll(ArrayUtils.addAll(
                    new byte[]{(byte) tlvFieldMeta.getTag()},
                    Bits.putShort((short) data.length)), data);
        } else {
            return null;
        }
    }

    @Override
    public String decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) throws Exception {
        return convertor.decode(bytes);
    }
}
