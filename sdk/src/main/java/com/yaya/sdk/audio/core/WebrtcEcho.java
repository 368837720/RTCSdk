package com.yaya.sdk.audio.core;

import com.yunva.jni.Native;

import java.util.LinkedList;

public class WebrtcEcho implements Echo {
	private final Object object;
	private short farData[];
	protected LinkedList<short[]> echoLinkedList;
	private int count;
	private short dstData[];
	private int echoFlag;

	public WebrtcEcho() {
		object = new Object();
		echoLinkedList = new LinkedList<>();
	}

	public void echoCreate(int delayParam) {
		count = 0;
		dstData = new short[320];
		farData = new short[160];
		Native.audio_process_create(8000, 160, delayParam);
		Native.audio_process_reset();
	}

	public void echoReset() {
		count = 0;
		dstData = new short[320];
		farData = new short[160];
		Native.audio_process_reset();
	}
	
	public void echoFarData(short playback[]) {
		if (echoFlag == 0)
		{
			Native.audio_process_farbuf(farData, farData.length);
			if (playback.length > 0)
				System.arraycopy(playback, 0, farData, 0, playback.length);
		}
	}

	public Boolean echoCancelProcess(short capture[], short aecNeared[],
									 short playbuffer[], int disableAecFlag) {
		short halfBuffer[] = new short[160];
//		int vadflag = Native.ailiaoProcess(capture, playbuffer, halfBuffer,
//				halfBuffer.length, disableAecFlag);
		Native.audio_process_run(capture, capture.length, halfBuffer,
				halfBuffer.length);
		
		int vadflag = 1;
//		
//		vadflag = 1;
//		if (vadflag == 1) {
//			System.arraycopy(halfBuffer, 0, dstData, count * 160,
//					halfBuffer.length);
//			count++;
//		}
//		if (count == 2) {
//			count = 0;
//			System.arraycopy(dstData, 0, aecNeared, 0, 320);
//			return Boolean.valueOf(true);
//		} else {
//			return Boolean.valueOf(false);
//		}
		if(vadflag == 1) {
			System.arraycopy(halfBuffer, 0, aecNeared, 0, 160);
//			System.arraycopy(capture, 0, aecNeared, 0, 160);
			return true;
		} else{
			return false;
		}
	}

	public void echoLinkedListClear() {
		synchronized (object) {
			echoLinkedList.clear();
		}
	}

	public Boolean echoCancel(short neared[], short aecNeared[],
							  boolean enableHardwareAecFlag) {
		echoFlag = 0;
		if (enableHardwareAecFlag)
			echoFlag = 1;
		return echoCancelProcess(neared, aecNeared, null, echoFlag);
	}

}