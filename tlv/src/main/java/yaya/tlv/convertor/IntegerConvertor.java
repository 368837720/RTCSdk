package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class IntegerConvertor implements Convertor<Integer, byte[]> {
    @Override
    public byte[] encode(Integer integer, Unsigned unsigned) {
        if (null == integer) {
            return null;
        } else if (unsigned.equals(Unsigned.UINT16)) {
            // uint16
            return Bits.putUInt16(integer);
        } else {
            return Bits.putInt(integer);
        }
    }

    @Override
    public Integer decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            if (bytes.length == Unsigned.length(Unsigned.UINT16, Integer.class)) {
                return Bits.getUInt16(bytes);
            } else {
                return Bits.getInt(bytes);
            }
        }
    }
}
