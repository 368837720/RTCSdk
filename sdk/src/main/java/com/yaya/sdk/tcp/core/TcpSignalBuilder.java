package com.yaya.sdk.tcp.core;

import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.tcp.SeqUtil;
import com.yaya.sdk.tlv.TlvUtil;
import com.yaya.sdk.tlv.protocol.LoginReq;
import com.yaya.sdk.tlv.protocol.LogoutReq;
import com.yaya.sdk.tlv.protocol.MicReq;
import com.yaya.sdk.tlv.protocol.ModeSettingReq;
import com.yaya.sdk.tlv.protocol.message.TextMessageReq;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageReq;
import com.yaya.sdk.util.TelephonyUtil;

import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/8.
 */
public class TcpSignalBuilder {

    private String mAppId;
    private SdkConfig mConfig;

    public TcpSignalBuilder(String appId, SdkConfig config) {
        mAppId = appId;
        mConfig = config;
    }

    public static TcpSignalBuilder with(String appId, SdkConfig config) {
        return new TcpSignalBuilder(appId, config);
    }

    public VoiceMessageReq buildVoiceMsgReq(byte[] data, long yunvaId,
                                            String troopsId, String expand) {
        VoiceMessageReq req = new VoiceMessageReq();
        req.setTroopsId(troopsId);
        req.setYunvaId(yunvaId);
        req.setMsg(data);
        req.setExpand(expand);
        addHeader(req);
        return req;
    }

    public LoginReq buildLoginReq(long yunvaId,
        String token, String troopsId, String troopsSeq) {
        LoginReq req = new LoginReq();
        req.setYunvaId(yunvaId);
        req.setToken(token);
        req.setTroopsId(troopsId);
        req.setAppId(mAppId);
        req.setSdkAppId(mConfig.getSdkAppId());
        req.setSdkAppVersion(mConfig.getSdkVersion());
        req.setRemark(null);
        req.setSeq(troopsSeq);
        req.setOsType(TelephonyUtil.getOsType());
        req.setOsVersion(TelephonyUtil.getSystemVersionName());
        addHeader(req);
        return req;
    }

    //1上麦；0下麦
    public MicReq buildMicReq(long yunvaId, String troopsId, String actionType) {
        MicReq req = new MicReq();
        req.setYunvaId(yunvaId);
        req.setTroopsId(troopsId);
        req.setActionType(actionType);
        addHeader(req);
        return req;
    }

    public ModeSettingReq buildModeSetReq(byte mode, byte leaderMode,
                                          long yunvaId, String nickname) {
        ModeSettingReq req = new ModeSettingReq();
        req.setYunvaId(yunvaId);
        req.setNickname(nickname);
        if(mode == 3) {
            req.setMode((byte) 1);
            req.setLeaderMode((byte) 1);
        } else {
            req.setMode(mode);
            req.setLeaderMode((byte) 0);
        }
        addHeader(req);
        return req;
    }

    public LogoutReq buildLogoutReq(String troopsId, long yunvaId) {
        LogoutReq req = new LogoutReq();
        req.setTroopsId(troopsId);
        req.setYunvaId(yunvaId);
        addHeader(req);
        return req;
    }

    public TextMessageReq buildTextMsgReq(
            String troopsId, long yunvaId, String text, String expand) {
        TextMessageReq req = new TextMessageReq();
        req.setTroopsId(troopsId);
        req.setRichText(null);
        req.setText(text);
        req.setYunvaId(yunvaId);
        req.setExpand(expand);
        addHeader(req);
        return req;
    }

    public static void addHeader(TlvSignal tlvSignal) {
        TlvAccessHeader header = TlvUtil.buildHeader(tlvSignal,
                SeqUtil.newSeq());
        tlvSignal.setHeader(header);
    }

}
