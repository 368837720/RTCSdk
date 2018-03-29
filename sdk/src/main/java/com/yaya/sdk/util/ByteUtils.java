package com.yaya.sdk.util;

import java.io.UnsupportedEncodingException;

/**
 * Byte level operations.
 * 
 * @author muli.zhu
 *
 */
public class ByteUtils {

	/**
	 * Reads a uint8 form bytes array
	 * 
	 * @param bytes
	 * @param pos
	 * @return
	 */
	public static int readUint8FromBytesArray(byte[] bytes, int pos) {
		if(bytes==null || bytes.length<=0){
			return 0;
		}
		return bytes[pos] & 0xff;
	}

	/**
	 * writes uint8 into bytesarray
	 * 
	 * @param bytes
	 * @param pos
	 * @param val
	 */
	public static void writeUint8ToBytesArray(byte[] bytes, int pos, int val) {
		bytes[pos] = (byte) (val & 0xff);
	}

	/**
	 * uint16
	 * 
	 * @param bytes
	 * @param pos
	 * @return
	 */
	public static int readUint16FromBytesArray(byte[] bytes, int pos) {
		if(bytes==null || bytes.length<=0){
			return 0;
		}
		
		if(bytes.length < 2){
			return readUint8FromBytesArray(bytes, 0);
		}
		
		return (((bytes[pos] & 0xff) << 8) + 
				(bytes[pos + 1] & 0xff));
	}

	public static void writeUint16ToBytesArray(byte[] bytes, int idx, int val) {
		bytes[idx] = (byte) ((val >> 8) & 0xff);
		bytes[idx + 1] = (byte) (val & 0xff);
	}

	/**
	 * uint32
	 * 
	 * @param bytes
	 * @param pos
	 * @return
	 */
	public static int readUint32FromBytesArray(byte[] bytes, int pos) {
		if(bytes == null || bytes.length <= 0){
			return 0;
		}
		
		if(bytes.length < 4){
			return readUint16FromBytesArray(bytes, 0);
		}
		
		return (((bytes[pos] & 0xff) << 24) + 
				((bytes[pos + 1] & 0xff) << 16) + 
				((bytes[pos + 2] & 0xff) << 8) + 
				(bytes[pos + 3] & 0xff));
	}

	public static void writeUint32ToBytesArray(byte[] bytes, int pos, int val) {
		bytes[pos] = (byte) ((val >> 24) & 0xff);
		bytes[pos + 1] = (byte) ((val >> 16) & 0xff);
		bytes[pos + 2] = (byte) ((val >> 8) & 0xff);
		bytes[pos + 3] = (byte) (val & 0xff);
	}
	
	public static void writeUint32ArrayToBytesArray(byte[] bytes, int pos, int[] val) {
		for(int i=0;i<val.length;i++){
			int v = val[i];
			bytes[pos + i*4] = (byte) ((v >> 24) & 0xff);
			bytes[pos + 1 + i*4] = (byte) ((v >> 16) & 0xff);
			bytes[pos + 2 + i*4] = (byte) ((v >> 8) & 0xff);
			bytes[pos + 3 + i*4] = (byte) (v & 0xff);
		}
	}

	/**
	 * 读取byte数组指定位置的内容
	 * 
	 * @param src 被读取的数组
	 * @param pos 读取的起始位置	
	 * @param len 读取数据的长度
	 * @return
	 */
	public static byte[] readBytesArray(byte[] src, int pos, int len) {
		byte[] ret = new byte[len];
		System.arraycopy(src, pos, ret, 0, len);
		return ret;
	}

	/**
	 * convert bytes array to utf-8 string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesArrayToString(byte[] bytes) {
		if(bytes==null){
			return "";
		}
		int i;
		for (i = 0; i < bytes.length && bytes[i] != 0; i++)
			;// ignore '\0'
		return new String(bytes, 0, i);
	}

	/**
	 * convert bytes array to gb2312 string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesArrayToGBString(byte[] bytes) {
		try {
			return new String(bytes, "GB2312");
		} catch (UnsupportedEncodingException e) {
		}
		return new String(bytes);
	}

	/**
	 * convert utf-8 string to gb2312 string
	 * 
	 * @param str
	 * @return
	 */
	public static String stringToGBString(String str) {
		return bytesArrayToGBString(str.getBytes());
	}

	/**
	 * convert bytes array to int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesArrayToInt(byte[] bytes) {
		int leng = bytes.length > 4 ? 4 : bytes.length;
		int ret = 0;
		for (int i = 0; i < leng; ++i) {
			ret = (ret << 8) + (bytes[i] & 0xff);
		}
		return ret;
	}

	/**
	 * append two bytes array
	 * 
	 * @param src1
	 * @param src2
	 * @return
	 */
	public static byte[] append(byte[] src1, byte[] src2) {
		byte[] ret = new byte[src1.length + src2.length];
		System.arraycopy(src1, 0, ret, 0, src1.length);
		System.arraycopy(src2, 0, ret, src1.length, src2.length);
		return ret;
	}

	/**
	 * merge a bytes array to current bytes array
	 * 
	 * @param bytes
	 * @param src
	 * @param pos
	 */
	public static void merge(byte[] bytes, byte[] src, int pos) {
		System.arraycopy(src, 0, bytes, pos, src.length);
	}
	

	/**
	 * reads bits to int
	 * 
	 * @param b
	 * @param pos
	 *            start position
	 * @param count
	 *            bit count
	 * @return
	 */
	public static int bitsToInt(byte b, int pos, int count) {
		byte tmp = (byte) (b >> (8 - pos - count));

		int ret = 0;
		byte mask = 0x01;
		for (int i = 0; i < count; i++) {
			ret = ret + (tmp & mask);
			mask = (byte) (mask << 1);
		}
		return ret;
	}
	
	public static byte[] shortsToByteArray(short[] shorts){
		byte[] shortsBuf = new byte[shorts.length*2];
		int n=0;
		for(int i=0;i<shorts.length;i++){
			byte[] shortBuf = shortToByte(shorts[i]);
			for(int j=0;j<2;j++){
				shortsBuf[n] = shortBuf[j];
				n++;
			}
		}
		return shortsBuf;
	}
	
	private static byte[] shortToByteArray(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}
	
	/** 
     * 注释：short到字节数组的转换！ 
     * 
     * @param number
     * @return 
     */ 
    public static byte[] shortToByte(short number) { 
        int temp = number; 
        byte[] b = new byte[2]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();//
            //将最低位保存在最低位 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
    
    private static void putShort(byte[] b, int off, short val) {
    	b[off + 1] = (byte) (val >>> 0);
    	b[off + 0] = (byte) (val >>> 8);
    }
	
	public static short[] bytesToShorts(byte[] source, int startPos,
			int byteLength) {
		short[] shorts = new short[byteLength/2];
		if (startPos < 0 || (byteLength%2 != 0) || source == null
				|| source.length < startPos + byteLength - 1){
			return null;
		}else{
			int j = 0;
			for(int i=0;i<byteLength/2;i++){
				shorts[i] = bytesToshort(source[j], source[j + 1], 2);
				j = j+2;
			}
		}
		return shorts;
	}

	public static short bytesToshort(byte b0, byte b1, int byteLength) {
		if (byteLength != 2)
			return 0;
		int i = 0;
		short out = 0;
		if (b0 < 0)
			i = 256 + b0;
		else
			i = b0;
		out += i;
		if (b1 < 0)
			i = 256 + b1;
		else
			i = b1;
		out += (i << 8);
		return out;
	}
		   
}
