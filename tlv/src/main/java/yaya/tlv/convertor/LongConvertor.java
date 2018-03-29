package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class LongConvertor implements Convertor<Long, byte[]> {
    @Override
    public byte[] encode(Long aLong, Unsigned unsigned) {
        if (null == aLong) {
            return null;
        } else if (unsigned.equals(Unsigned.UINT32)) {
            // uint32
            return Bits.putUInt32(aLong);
        } else {
            return Bits.putLong(aLong);
        }
    }

    @Override
    public Long decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            if (bytes.length == Unsigned.length(Unsigned.UINT32, Long.class)) {
                return Bits.getUInt32(bytes);
            } else {
                return Bits.getLong(bytes);
            }
        }
    }
}
