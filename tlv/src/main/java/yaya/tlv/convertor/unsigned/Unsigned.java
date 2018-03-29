package yaya.tlv.convertor.unsigned;

public enum Unsigned {
    NONE, UINT8, UINT16, UINT32;

    public static int length(Unsigned unsigned, Class type) {
        if (unsigned.equals(UINT8)) {
            return 1;
        } else if (unsigned.equals(UINT16)) {
            return 2;
        } else if (unsigned.equals(UINT32)) {
            return 4;
        } else {
            if (Byte.class.isAssignableFrom(type) || type.equals(byte.class)) {
                return 1;
            } else if (Short.class.isAssignableFrom(type)
                    || type.equals(short.class)) {
                return 2;
            } else if (Integer.class.isAssignableFrom(type)
                    || type.equals(int.class)) {
                return 4;
            } else if (Long.class.isAssignableFrom(type)
                    || type.equals(long.class)) {
                return 8;
            } else {
                throw new RuntimeException("not support type:" + type);
            }
        }
    }
}
