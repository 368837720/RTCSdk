package com.yaya.sdk.tlv;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tlv.protocol.HeartbeatResp;
import com.yaya.sdk.tlv.protocol.LoginReq;
import com.yaya.sdk.tlv.protocol.LoginResp;
import com.yaya.sdk.tlv.protocol.LogoutReq;
import com.yaya.sdk.tlv.protocol.LogoutResp;
import com.yaya.sdk.tlv.protocol.MicReq;
import com.yaya.sdk.tlv.protocol.MicResp;
import com.yaya.sdk.tlv.protocol.ModeChangeNotify;
import com.yaya.sdk.tlv.protocol.ModeSettingReq;
import com.yaya.sdk.tlv.protocol.ModeSettingResp;
import com.yaya.sdk.tlv.protocol.TroopsKickOutNotify;
import com.yaya.sdk.tlv.protocol.UserStateNotify;
import com.yaya.sdk.tlv.protocol.info.AuthReq;
import com.yaya.sdk.tlv.protocol.info.AuthResp;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoReq;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoResp;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoReq;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;
import com.yaya.sdk.tlv.protocol.info.RegisterReq;
import com.yaya.sdk.tlv.protocol.info.RegisterResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthReq;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthResp;
import com.yaya.sdk.tlv.protocol.init.GetModelParamReq;
import com.yaya.sdk.tlv.protocol.init.GetModelParamResp;
import com.yaya.sdk.tlv.protocol.init.Param;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;
import com.yaya.sdk.tlv.protocol.message.TextMessageReq;
import com.yaya.sdk.tlv.protocol.message.TextMessageResp;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageReq;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageResp;

import yaya.tlv.DefaultTlvStore;
import yaya.tlv.TlvStore;

/**
 * Created by ober on 2016/10/31.
 */
public class YayaTlvStore extends DefaultTlvStore {

    private static final String TAG ="YayaTlvStore";

    private static YayaTlvStore instance;

    public static TlvStore getInstance() {
        if(instance == null) {
            try {
                instance = new YayaTlvStore();
            } catch (Exception e) {
                MLog.e(TAG, e.getMessage());
            }
        }
        return instance;
    }

    /**
     * 初始化添加Tlv协议
     */
    private void initRegisterProtocol() throws Exception {
        addTypeMetaCache(GetGmgcUserInfoReq.class);
        addTypeMetaCache(GetGmgcUserInfoResp.class);
        addTypeMetaCache(ThirdAuthReq.class);
        addTypeMetaCache(ThirdAuthResp.class);
        addTypeMetaCache(RegisterReq.class);
        addTypeMetaCache(RegisterResp.class);
        addTypeMetaCache(AuthReq.class);
        addTypeMetaCache(AuthResp.class);
        addTypeMetaCache(GetTroopsInfoReq.class);
        addTypeMetaCache(GetTroopsInfoResp.class);
        addTypeMetaCache(HeartbeatResp.class);
        addTypeMetaCache(LoginReq.class);
        addTypeMetaCache(LoginResp.class);
        addTypeMetaCache(LogoutReq.class);
        addTypeMetaCache(LogoutResp.class);
        addTypeMetaCache(MicReq.class);
        addTypeMetaCache(MicResp.class);
        addTypeMetaCache(TextMessageNotify.class);
        addTypeMetaCache(TextMessageReq.class);
        addTypeMetaCache(TextMessageResp.class);
        addTypeMetaCache(VoiceMessageNotify.class);
        addTypeMetaCache(VoiceMessageReq.class);
        addTypeMetaCache(VoiceMessageResp.class);
        addTypeMetaCache(UserStateNotify.class);
        addTypeMetaCache(TroopsKickOutNotify.class);
        //201409031945
        addTypeMetaCache(ModeSettingReq.class);
        addTypeMetaCache(ModeSettingResp.class);
        addTypeMetaCache(ModeChangeNotify.class);
        addTypeMetaCache(GetModelParamReq.class);
        addTypeMetaCache(GetModelParamResp.class);
        addTypeMetaCache(Param.class);
    }

    private YayaTlvStore() throws Exception {
        super();
        initRegisterProtocol();
    }
}
