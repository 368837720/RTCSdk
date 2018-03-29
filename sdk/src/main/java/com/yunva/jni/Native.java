// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2015-12-22 12:52:01
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Native.java

// Referenced classes of package com.gotye.jni:
//            JitterBufferPacket

package com.yunva.jni;


public class Native
{

	public static native int audio_process_create(int frequency, int framesize, int delay);

	public static native long audio_buffer_init(int paramInt);
	  
	public static native void audio_buffer_destroy(long paramLong);
	  
	public static native void audio_buffer_put(long paramLong, JitterBufferPacket paramJitterBufferPacket);
	  
	public static native int audio_buffer_get(long paramLong, JitterBufferPacket paramJitterBufferPacket, int paramInt, int[] paramArrayOfInt);
	  
	public static native int audio_buffer_get_pointer_timestamp(long paramLong);
	  
	public static native void audio_buffer_tick(long paramLong);
	  
	public static native int audio_buffer_ctl(long paramLong, int paramInt, int[] paramArrayOfInt);
	  
	public static native int audio_buffer_update_delay(long paramLong, JitterBufferPacket paramJitterBufferPacket, int[] paramArrayOfInt);

	public static native void audio_process_reset();
	
	public static native void audio_process_close();
	  
	public static native int audio_process_run(short[] paramArrayOfShort1, int paramInt1, short[] paramArrayOfShort2, int paramInt2);
	  
	public static native int audio_process_runx(short[] in, int inlen, short[] out, int outlen);
	
	public static native void audio_process_farbuf(short[] paramArrayOfShort, int paramInt);

	public static native void audio_mix_normalizing_close(short[] pcm0, short[] pcm1, short[] pcm2, short[] pcm3, short[] pcm4, short[] resultPcm, int mixlen);
	
	public static native void audio_mix_simple_close(short[] pcm0, short[] pcm1, short[] pcm2, short[] pcm3, short[] pcm4, short[] resultPcm, int mixlen);
	
	
	public static native void codec_AmrEncoder_open();
	public static native void codec_AmrEncoder_close();
	public static native int codec_AmrEncoder_pcm2amr(byte[] pcmBuf,
			int pcmBufSize, byte[] amrBuf, int amrBufSize);
	
	public static native void codec_AmrDecoder_open();
	public static native void codec_AmrDecoder_close();
	public static native int codec_AmrDecoder_amr2pcm(byte[] amrBuf,
			int amrBufSize, short[] pcmBuf, int pcmBufSize,
			int[] consumeAndOutSize);
	
	public static native void codec_AmrDecoder1_open();
	public static native void codec_AmrDecoder1_close();
	public static native int codec_AmrDecoder1_amr2pcm(byte[] amrBuf,
			int amrBufSize, short[] pcmBuf, int pcmBufSize,
			int[] consumeAndOutSize);
	
	public static native void codec_AmrDecoder2_open();
	public static native void codec_AmrDecoder2_close();
	public static native int codec_AmrDecoder2_amr2pcm(byte[] amrBuf,
			int amrBufSize, short[] pcmBuf, int pcmBufSize,
			int[] consumeAndOutSize);
	
	public static native void codec_AmrDecoder3_open();
	public static native void codec_AmrDecoder3_close();
	public static native int codec_AmrDecoder3_amr2pcm(byte[] amrBuf,
			int amrBufSize, short[] pcmBuf, int pcmBufSize,
			int[] consumeAndOutSize);
	
	public static native void codec_AmrDecoder4_open();
	public static native void codec_AmrDecoder4_close();
	public static native int codec_AmrDecoder4_amr2pcm(byte[] amrBuf,
			int amrBufSize, short[] pcmBuf, int pcmBufSize,
			int[] consumeAndOutSize);
	
}