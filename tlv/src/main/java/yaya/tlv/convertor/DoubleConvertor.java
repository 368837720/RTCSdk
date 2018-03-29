package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class DoubleConvertor implements Convertor<Double, byte[]> {
    @Override
    public byte[] encode(Double aDouble, Unsigned unsigned) {
        if (null == aDouble) {
            return null;
        } else {
            return Bits.putDouble(aDouble);
        }
    }

    @Override
    public Double decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            return Bits.getDouble(bytes);
        }
    }
}
