package yaya.tlv.convertor;

import java.io.UnsupportedEncodingException;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.util.StringUtils;

public class StringConvertor implements Convertor<String, byte[]> {
    private final String encoding = "UTF-8";

    @Override
    public byte[] encode(String s, Unsigned unsigned) {
        if (null == s) {
            return null;
        }
        try {
            if (s.endsWith("\0")) {
                return s.getBytes(encoding);
            } else {
                return s.concat("\0").getBytes(encoding);
            }
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }

    @Override
    public String decode(byte[] bytes) {
        if (null != bytes) {
            return StringUtils.removeEnd(new String(bytes), "\0");
        } else {
            return null;
        }
    }
}
