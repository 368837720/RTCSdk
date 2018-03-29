package com.yaya.sdk.audio.core;


public interface Echo
{

    void echoReset();

    void echoFarData(short playback[]);

    void echoLinkedListClear();

    Boolean echoCancel(short neared[], short aecNeared[], boolean enableHardwareAecFlag);

}
