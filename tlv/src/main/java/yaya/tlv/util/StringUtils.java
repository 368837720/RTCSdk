package yaya.tlv.util;

public class StringUtils {

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static String replace(String text, String searchString,
                                 String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString,
                                 String replacement, int max) {
        if ((isEmpty(text)) || (isEmpty(searchString)) || (replacement == null)
                || (max == 0)) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0) ? 0 : increase;
        increase *= ((max > 64) ? 64 : (max < 0) ? 16 : max);
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return cs1 != null && ((cs2 == null) || cs1.equals(cs2));
    }

    public static String removeEnd(String str, String remove) {
        if ((isEmpty(str)) || (isEmpty(remove))) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

}
