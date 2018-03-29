package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;

public class ByteConvertor implements Convertor<Byte, byte[]> {
    @Override
    public byte[] encode(Byte aByte, Unsigned unsigned) {
        if (null == aByte) {
            return null;
        } else {
            return new byte[]{aByte};
        }
    }

    @Override
    public Byte decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        return bytes[0];
    }
}
