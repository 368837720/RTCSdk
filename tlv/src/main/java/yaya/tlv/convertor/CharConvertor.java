package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.Bits;

public class CharConvertor implements Convertor<Character, byte[]> {
    @Override
    public byte[] encode(Character character, Unsigned unsigned) {
        if (null == character) {
            return null;
        } else {
            return Bits.putChar(character);
        }
    }

    @Override
    public Character decode(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            return Bits.getChar(bytes);
        }
    }
}
