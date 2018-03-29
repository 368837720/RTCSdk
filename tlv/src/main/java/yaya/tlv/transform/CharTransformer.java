package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

public class CharTransformer implements Transformer<Character, byte[]> {
    private Convertor<Character, byte[]> convertor;

    public CharTransformer() {
        try {
            convertor = ConvertorFactory.build(Character.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode(Character character, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) {
        if (null == character) {
            return null;
        } else {
            byte[] data = convertor.encode(character,
                    tlvFieldMeta.getUnsigned());
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
    public Character decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                            TlvStore tlvStore) {
        return convertor.decode(bytes);
    }
}
