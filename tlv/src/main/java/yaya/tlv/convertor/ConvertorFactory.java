package yaya.tlv.convertor;

public class ConvertorFactory {
    private static ByteConvertor byteConvertor = new ByteConvertor();
    private static CharConvertor charConvertor = new CharConvertor();
    private static ShortConvertor shortConvertor = new ShortConvertor();
    private static IntegerConvertor integerConvertor = new IntegerConvertor();
    private static LongConvertor longConvertor = new LongConvertor();
    private static FloatConvertor floatConvertor = new FloatConvertor();
    private static DoubleConvertor doubleConvertor = new DoubleConvertor();
    private static StringConvertor stringConvertor = new StringConvertor();

    public static Convertor build(Class type) throws Exception {
        if (Byte.class.isAssignableFrom(type)) {
            return byteConvertor;
        } else if (Character.class.isAssignableFrom(type)) {
            return charConvertor;
        } else if (Short.class.isAssignableFrom(type)) {
            return shortConvertor;
        } else if (Integer.class.isAssignableFrom(type)) {
            return integerConvertor;
        } else if (Long.class.isAssignableFrom(type)) {
            return longConvertor;
        } else if (Float.class.isAssignableFrom(type)) {
            return floatConvertor;
        } else if (Double.class.isAssignableFrom(type)) {
            return doubleConvertor;
        } else if (String.class.isAssignableFrom(type)) {
            return stringConvertor;
        } else {
            throw new Exception("type:" + type + " isn't implement.");
        }
    }
}
