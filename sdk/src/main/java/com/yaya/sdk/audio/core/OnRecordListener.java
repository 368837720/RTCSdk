package com.yaya.sdk.audio.core;

public interface OnRecordListener {
	void record(byte[] data);
	void recordUnavailable(int read);
	void recordFinish();
	void recordStart();
}
