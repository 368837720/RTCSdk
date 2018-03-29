package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class FloatConvertor implements Convertor<Float, byte[]> {
    @Override
    public byte[] encode(Float aFloat, Unsigned unsigned) {
        if (null == aFloat) {
            return null;
        } else {
            return Bits.putFloat(aFloat);
        }
    }

    @Override
    public Float decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            return Bits.getFloat(bytes);
        }
    }
}
