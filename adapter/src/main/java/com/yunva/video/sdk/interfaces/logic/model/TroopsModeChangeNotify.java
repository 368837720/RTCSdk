package com.yunva.video.sdk.interfaces.logic.model;

public class TroopsModeChangeNotify {
	
	private int modeType;//0自由模式，1抢麦模式，2指挥模式
	private boolean isLeader;//自己是否是队长
	public int getModeType() {
		return modeType;
	}
	public void setModeType(int modeType) {
		this.modeType = modeType;
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	@Override
	public String toString() {
		return "TroopsModeChangeNotify [modeType=" + modeType + ", isLeader="
				+ isLeader + "]";
	}
	
}
