package com.yaya.sdk.audio.core;

public class AudioWebrtcTool {

	private static final String TAG = "AudioWebrtcTool";

	public static final Object ECHO_LOCK = new Object();

	public static WebrtcEcho m_ec;
	
	public static void echoServiceCreart(int delayParam) {
		if (m_ec == null) {
			synchronized (ECHO_LOCK) {
				m_ec = new WebrtcEcho();
				m_ec.echoCreate(delayParam);
			}
		}
	}

}
