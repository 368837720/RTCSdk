package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class ShortConvertor implements Convertor<Short, byte[]> {
    @Override
    public byte[] encode(Short aShort, Unsigned unsigned) {
        if (null == aShort) {
            return null;
        } else if (unsigned.equals(Unsigned.UINT8)) {
            // uint 8
            return Bits.putUInt8(aShort);
        } else {
            return Bits.putShort(aShort);
        }
    }

    @Override
    public Short decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else if (bytes.length == Unsigned.length(Unsigned.UINT8, Short.class)) {
            // uint8
            return Bits.getUInt8(bytes);
        } else {
            return Bits.getShort(bytes);
        }
    }
}
