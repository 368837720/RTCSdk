package yaya.tlv.util;

/**
 * Created by wy on 14-3-18.
 */
public class Bits {
    public static boolean getBoolean(byte[] b) {
        return b[0] != 0;
    }

    public static byte[] putUInt32(long n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static long getUInt32(byte[] array) {
        return ((long) (array[3] & 0xff)) | ((long) (array[2] & 0xff)) << 8
                | ((long) (array[1] & 0xff)) << 16
                | ((long) (array[0] & 0xff)) << 24;
    }

    public static byte[] putUInt8(short n) {
        byte[] b = new byte[1];
        b[0] = (byte) (n & 0xff);
        return b;
    }

    public static short getUInt8(byte[] array) {
        return (short) (array[0] & 0xff);
    }

    public static byte[] putUInt16(int n) {
        byte[] array = new byte[2];
        array[1] = (byte) (n & 0xff);
        array[0] = (byte) ((n >> 8) & 0xff);
        return array;
    }

    public static int getUInt16(byte b[]) {
        return b[1] & 0xff | (b[0] & 0xff) << 8;
    }

    public static char getChar(byte[] b) {
        return (char) (((b[1] & 0xFF) << 0) + ((b[0]) << 8));
    }

    public static short getShort(byte[] b) {
        return (short) (((b[1] & 0xFF) << 0) + ((b[0]) << 8));
    }

    public static int getInt(byte[] b) {
        return ((b[3] & 0xFF) << 0) + ((b[2] & 0xFF) << 8)
                + ((b[1] & 0xFF) << 16) + ((b[0]) << 24);
    }

    public static float getFloat(byte[] b) {
        int i = ((b[3] & 0xFF) << 0) + ((b[2] & 0xFF) << 8)
                + ((b[1] & 0xFF) << 16) + ((b[0]) << 24);
        return Float.intBitsToFloat(i);
    }

    public static long getLong(byte[] b) {
        return ((b[7] & 0xFFL) << 0) + ((b[6] & 0xFFL) << 8)
                + ((b[5] & 0xFFL) << 16) + ((b[4] & 0xFFL) << 24)
                + ((b[3] & 0xFFL) << 32) + ((b[2] & 0xFFL) << 40)
                + ((b[1] & 0xFFL) << 48) + (((long) b[0]) << 56);
    }

    public static double getDouble(byte[] b) {
        long j = ((b[7] & 0xFFL) << 0) + ((b[6] & 0xFFL) << 8)
                + ((b[5] & 0xFFL) << 16) + ((b[4] & 0xFFL) << 24)
                + ((b[3] & 0xFFL) << 32) + ((b[2] & 0xFFL) << 40)
                + ((b[1] & 0xFFL) << 48) + (((long) b[0]) << 56);
        return Double.longBitsToDouble(j);
    }

	/*
     * Methods for packing primitive values into byte arrays starting at given
	 * offsets.
	 */

    public static byte[] putBoolean(boolean val) {
        byte[] b = new byte[1];
        b[0] = (byte) (val ? 1 : 0);
        return b;
    }

    public static byte[] putChar(char val) {
        byte[] b = new byte[2];
        b[1] = (byte) (val >>> 0);
        b[0] = (byte) (val >>> 8);
        return b;
    }

    public static byte[] putShort(short val) {
        byte[] b = new byte[2];
        b[1] = (byte) (val >>> 0);
        b[0] = (byte) (val >>> 8);
        return b;
    }

    public static byte[] putInt(int val) {
        byte[] b = new byte[4];
        b[3] = (byte) (val >>> 0);
        b[2] = (byte) (val >>> 8);
        b[1] = (byte) (val >>> 16);
        b[0] = (byte) (val >>> 24);
        return b;
    }

    public static byte[] putFloat(float val) {
        byte[] b = new byte[4];
        int i = Float.floatToIntBits(val);
        b[3] = (byte) (i >>> 0);
        b[2] = (byte) (i >>> 8);
        b[1] = (byte) (i >>> 16);
        b[0] = (byte) (i >>> 24);
        return b;
    }

    public static byte[] putLong(long val) {
        byte[] b = new byte[8];
        b[7] = (byte) (val >>> 0);
        b[6] = (byte) (val >>> 8);
        b[5] = (byte) (val >>> 16);
        b[4] = (byte) (val >>> 24);
        b[3] = (byte) (val >>> 32);
        b[2] = (byte) (val >>> 40);
        b[1] = (byte) (val >>> 48);
        b[0] = (byte) (val >>> 56);
        return b;
    }

    public static byte[] putDouble(double val) {
        byte[] b = new byte[8];
        long j = Double.doubleToLongBits(val);
        b[7] = (byte) (j >>> 0);
        b[6] = (byte) (j >>> 8);
        b[5] = (byte) (j >>> 16);
        b[4] = (byte) (j >>> 24);
        b[3] = (byte) (j >>> 32);
        b[2] = (byte) (j >>> 40);
        b[1] = (byte) (j >>> 48);
        b[0] = (byte) (j >>> 56);
        return b;
    }
}
