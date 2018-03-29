package com.yaya.sdk.room.troop;

/**
 * Created by ober on 2016/10/28.
 */
public interface ITroops {
    /**
     * 获取房间信息 @see {@link com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp}
     * @param seq 房间Id 不为null
     * @param appId 用户的appId
     * @param callback 回调
     */
    void getTroopsInfo(String seq, String appId, GetTroopCallback callback);
}
