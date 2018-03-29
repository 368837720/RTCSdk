package yaya.tlv.transform;

import yaya.tlv.signal.TlvSignal;

public class TransformerFactory {
    private static ByteTransformer byteTransformer = new ByteTransformer();
    private static ShortTransformer shortTransformer = new ShortTransformer();
    private static CharTransformer charTransformer = new CharTransformer();
    private static IntTransformer intTransformer = new IntTransformer();
    private static LongTransformer longTransformer = new LongTransformer();
    private static FloatTransformer floatTransformer = new FloatTransformer();
    private static DoubleTransformer doubleTransformer = new DoubleTransformer();
    private static TlvSignalTransformer tlvSignalTransformer = new TlvSignalTransformer();
    private static StringTransformer stringTransformer = new StringTransformer();
    private static ByteArrayTransformer byteArrayTransformer = new ByteArrayTransformer();

    public static Transformer build(Class type) throws Exception {
        if (Byte.class.isAssignableFrom(type) || type.equals(byte.class)) {
            return byteTransformer;
        } else if (Character.class.isAssignableFrom(type)
                || type.equals(char.class)) {
            return charTransformer;
        } else if (Short.class.isAssignableFrom(type)
                || type.equals(short.class)) {
            return shortTransformer;
        } else if (Integer.class.isAssignableFrom(type)
                || type.equals(int.class)) {
            return intTransformer;
        } else if (Long.class.isAssignableFrom(type) || type.equals(long.class)) {
            return longTransformer;
        } else if (Float.class.isAssignableFrom(type)
                || type.equals(float.class)) {
            return floatTransformer;
        } else if (Double.class.isAssignableFrom(type)
                || type.equals(double.class)) {
            return doubleTransformer;
        } else if (String.class.isAssignableFrom(type)) {
            return stringTransformer;
        } else if (TlvSignal.class.isAssignableFrom(type)) {
            return tlvSignalTransformer;
        } else if (type.isArray() && type.getComponentType().equals(byte.class)) {
            return byteArrayTransformer;
        } else {
            throw new Exception("type:" + type + " isn't implement.");
        }
    }
}
