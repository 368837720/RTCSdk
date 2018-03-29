package com.yaya.sdk;

/**
 * 网络状态监听者<br/>
 * 用于监听 客户端--Yaya语音服务端 间的连接状态 <br/>
 * 当语音房间tcp处于连接状态，该方法会周期性回调，回调周期为{@link #UPDATE_PERIOD}
 */
public interface YayaNetStateListener {
    /**
     * 回调周期
     */
    long UPDATE_PERIOD = 15000; //心跳周期

    long STATE_NORMAL = 100;
    long STATE_MAY_BE_NORMAL = 140;
    long STATE_BAD = 180;
    long STATE_ERR = 300;

    /**
     * state = recv - send可获取网络状态, state数值越小代表网络状态越好
     * state<{@link #STATE_NORMAL} 网络正常
     * state<{@link #STATE_MAY_BE_NORMAL} 接近正常
     * state<{@link #STATE_BAD} 网络较差
     * state<{@link #STATE_ERR} 网络很差
     * state>{@link #STATE_ERR} 网络可能异常
     *
     * @param send 状态signal request时间戳
     * @param recv 状态signal response时间戳
     */
    void onNetStateUpdate(long send, long recv);
}
