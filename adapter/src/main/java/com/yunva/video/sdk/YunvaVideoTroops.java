package com.yunva.video.sdk;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Surface;

import com.yaya.sdk.MLog;
import com.yaya.sdk.RTV;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;
import com.yunva.video.sdk.interfaces.logic.model.AudioRecordUnavailableNotify;
import com.yunva.video.sdk.interfaces.logic.model.AuthResp;
import com.yunva.video.sdk.interfaces.logic.model.LoginResp;
import com.yunva.video.sdk.interfaces.logic.model.ModeSettingResp;
import com.yunva.video.sdk.interfaces.logic.model.RealTimeVoiceMessageNotify;
import com.yunva.video.sdk.interfaces.logic.model.TextMessageResp;
import com.yunva.video.sdk.interfaces.logic.model.TroopsModeChangeNotify;

import java.util.List;

/**
 * 该类为了兼容老版本
 * Created by ober on 2016/11/28.
 */
public class YunvaVideoTroops {
    private static final String TAG = "YunvaVideoTroops";

    private static YunvaVideoTroops mInstance;

    private ListenerAdapter listenerAdapter;

    private YunvaVideoTroops(ListenerAdapter adapter) {
        this.listenerAdapter = adapter;
    }

    public static boolean initApplicationOnCreate(Application app, String appId) {
        Log.d(TAG, "initApplicationOnCreate");
        return true;
    }

    public static YunvaVideoTroops getInstance(Context context, String appId, VideoTroopsRespondListener oldListener,
                                               int isTest, boolean hasVideo) {

        ListenerAdapter listenerAdapter = new ListenerAdapter(oldListener);
        RTV.Env env;
        if(isTest == 1) {
            env = RTV.Env.Product;
        } else if(isTest == 2) {
            env = RTV.Env.Oversea;
        } else {
            env = RTV.Env.Test;
        }

        if(mInstance == null) {
            mInstance = new YunvaVideoTroops(listenerAdapter);
        }
        YayaRTV.getInstance().init(context, appId, listenerAdapter, env, RTV.Mode.Robmic);

        return mInstance;
    }

    public static YunvaVideoTroops getInstance(Context context, String appId,
                                               VideoTroopsRespondListener oldListener,
                                               boolean isTest, boolean hasVideo) {
        ListenerAdapter listenerAdapter = new ListenerAdapter(oldListener);
        RTV.Env env;
        if(!isTest) {
            env = RTV.Env.Product;
        } else {
            env = RTV.Env.Test;
        }
        if(mInstance == null) {
            mInstance = new YunvaVideoTroops(listenerAdapter);
        }

        YayaRTV.getInstance().init(context, appId, listenerAdapter, env, RTV.Mode.Robmic);

        return mInstance;
    }

    public static YunvaVideoTroops getInstance() {
        return mInstance;
    }

    public int getAdaptVersion() {
        return 1;
    }

    public void onDestroy() {
        try {
            YayaRTV.getInstance().destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void login(String seq) {
        YayaRTV.getInstance().login(seq);
    }

    public void login(Long yunvaId, String password, String seq) {
        YayaRTV.getInstance().login(yunvaId, password, seq);
    }

    public void thirdAuth(Long yunvaId, String t,
                          String thirdId, String thirdName, String seq) {
        YayaRTV.getInstance().thirdAuth(yunvaId, t, thirdId, thirdName, seq);
    }

    public void login(String seq, boolean hasVideo, byte position, int videoCount) {
        YayaRTV.getInstance().login(seq);
    }

    public void loginBinding(String tt, String seq, boolean hasVideo, byte position, int videoCount) {
        YayaRTV.getInstance().loginBinding(tt, seq);
    }

    public void logout() {
        YayaRTV.getInstance().logout();
    }

    public void mic(String actionType, String expand) {
        YayaRTV.getInstance().mic(actionType, expand);
    }

    public void sendTextMessage(String text, String expand) {
        YayaRTV.getInstance().sendTextMessage(text, expand);
    }

    public void sendTextMessage(String text, String voiceUrl, long voiceDuration, String expand) {
        MLog.e(TAG, "不支持该接口 sendTextMessage voiceUrl");
    }

    public void setPausePlayAudio(boolean isPasuePlay) {
        MLog.e(TAG, "不支持该接口 setPausePlayAudio");
    }

    public boolean isPausePlayAudio() {
        MLog.e(TAG, "不支持该接口 isPausePlayAudio");
        return false;
    }

    public void uploadVoiceMessage(final String filePath, final long voiceDuration, final String expand) {
        MLog.e(TAG, "不支持该接口 uploadVoiceMessage");
    }

    public void setAutoPlayVoiceMessage(boolean isAutoPlay) {
        MLog.e(TAG, "不支持该接口 setAutoPlayVoiceMessage");
    }

    public boolean isAutoPlayVoiceMessage(){
        MLog.e(TAG, "不支持该接口 isAutoPlayVoiceMessage");
        return false;
    }

    public void setNativeSurface_0(Surface surface, int width, int height, int format) {
        MLog.e(TAG, "不支持该接口 setNativeSurface_0");
    }

    public void setNativeSurface_1(Surface surface, int width, int height, int format) {
        MLog.e(TAG, "不支持该接口 setNativeSurface_1");
    }

    public void setNativeSurface_2(Surface surface, int width, int height, int format) {
        MLog.e(TAG, "不支持该接口 setNativeSurface_2");
    }

    public void setNativeSurface_3(Surface surface, int width, int height, int format) {
        MLog.e(TAG, "不支持该接口 setNativeSurface_3");
    }

    public void inviteUserVideo(String nickname, String ext, List<Byte> invitees) {
        MLog.e(TAG, "不支持该接口 inviteUserVideo");
    }

    public void inviteUserVideoResp(String nickname, String ext, Byte inviter, Byte state, String remark) {
        MLog.e(TAG, "不支持该接口 inviteUserVideoResp");
    }

    public void videoStateNotifyReq(String nickname, String ext, Byte state) {
        MLog.e(TAG, "不支持该接口 videoStateNotifyReq");
    }

    public void setVideoSize(int width, int height) {
        MLog.e(TAG, "不支持该接口 setVideoSize");
    }

    public String reportVideo(byte position) {
        MLog.e(TAG, "不支持该接口 reportVideo");
        return null;
    }

    public void queryUserList() {
        MLog.e(TAG, "不支持该接口 queryUserList");
    }

    public void queryBlackUserReq(Byte position) {
        MLog.e(TAG, "不支持该接口 queryBlackUserReq");
    }

    public void setVideoFrameRate(int frameRate) {
        MLog.e(TAG, "不支持该接口 setVideoFrameRate");
    }

    public void mute() {
        MLog.e(TAG, "不支持该接口 mute");
    }

    public void unMute() {
        MLog.e(TAG, "不支持该接口 unMute");
    }

    public void setAutoMute(boolean isAutoMute) {
        MLog.e(TAG, "不支持该接口 setAutoMute");
    }

    public void setSendStateReq(String sendState) {
        MLog.e(TAG, "不支持该接口 setSendStateReq");
    }

    public void modeSettingReq(Byte mode, Byte leaderMode) {
        RTV.Mode m;
        if(leaderMode != null && leaderMode == 1) {
            m = RTV.Mode.Leader;
        } else if(mode == 0) {
            m = RTV.Mode.Free;
        } else {
            m = RTV.Mode.Robmic;
        }
        YayaRTV.getInstance().modeSettingReq(m);
    }

    public void setCloseCallsToMonitor(boolean isClose) {
        MLog.e(TAG, "不支持该接口 setCloseCallsToMonitor");
    }

    public void setAudioPlayStreamType(int streamType) {
        MLog.e(TAG, "不支持该接口 setAudioPlayStreamType");
    }

    public void encoderBdRecordVoiceEvent(byte[] data, Integer result, long voiceDuration) {
        MLog.e(TAG, "不支持该接口 encoderBdRecordVoiceEvent");
    }

    public void sendBdTextMessage(String text, String richText, String expand) {
        MLog.e(TAG, "不支持该接口 sendBdTextMessage");
    }

    public void sendRichMessage(String text,String filePath,Long voiceDuration,String expand){
        MLog.e(TAG, "不支持该接口 sendRichMessage");
    }

    public void onBdErrorReset() {
        MLog.e(TAG, "不支持该接口 onBdErrorReset");
    }

    public void uploadVoiceMessage(final String filePath){
        MLog.e(TAG, "不支持该接口 uploadVoiceMessage");
    }

    public void uploadVoiceFile(String voiceFilePath, String fileRetainTimeType) {
        MLog.e(TAG, "不支持该接口 uploadVoiceFile");
    }

    public void uploadVoiceFile(String voiceFilePath, String fileRetainTimeType, String expand) {
        MLog.e(TAG, "不支持该接口 uploadVoiceFile");
    }

    public void uploadPictureFile(String picFilePath, String picType, int scaleToSize, String fileRetainTimeType) {
        MLog.e(TAG, "不支持该接口 uploadPictureFile");
    }

    public void queryHistoryMsgReq(int page, int size){
        MLog.e(TAG, "不支持该接口 queryHistoryMsgReq");
    }

    public void setUploadVoiceDataTime(String storeTime){
        MLog.e(TAG, "不支持该接口 setUploadVoiceDataTime");
    }

    public void setIsNotUploadBdVoiceData(boolean isNotUploadBdVoiceData){
        MLog.e(TAG, "不支持该接口 setIsNotUploadBdVoiceData");
    }

    public void httpVoiceRecognizeReq(String recognizeLanguage, String outputTextLanguageType, String voiceFilePath, long voiceDuration, String fileRetainTimeType, String voiceUrl, String expand) {
        MLog.e(TAG, "不支持该接口 httpVoiceRecognizeReq");
    }

    private static class ListenerAdapter implements com.yaya.sdk.VideoTroopsRespondListener {

        VideoTroopsRespondListener old;

        ListenerAdapter(VideoTroopsRespondListener old) {
            this.old = old;
        }

        VideoTroopsRespondListener getOld() {
            return old;
        }

        @Override
        public void initComplete() {
            old.initComplete();
        }

        @Override
        public void onLoginResp(int result, String msg, long yunvaId, byte mode) {
            LoginResp loginResp = new LoginResp();
            if(result == 0) {
                loginResp.setResult(0L);
                loginResp.setYunvaId(yunvaId);
                loginResp.setModeType(mode);
            } else {
                loginResp.setResult((long)result);
                loginResp.setMsg("登录失败:" + msg);
            }
            old.onLoginResp(loginResp);
        }

        @Override
        public void onLogoutResp(long result, String msg) {
            old.onLogoutResp((int)result, msg);
        }

        @Override
        public void onMicResp(long result, String msg, String actionType) {
            old.onMicResp((int)result, msg, actionType);
        }

        @Override
        public void onModeSettingResp(long result, String msg, RTV.Mode mode) {
            ModeSettingResp resp = new ModeSettingResp();
            resp.setMsg(msg);
            resp.setResult(result);
            old.onModeSettingResp(resp);
        }

        @Override
        public void onSendRealTimeVoiceMessageResp(long result, String msg) {
            old.onSendRealTimeVoiceMessageResp((int)result, msg);
        }

        @Override
        public void onTextMessageNotify(TextMessageNotify textMessageNotify) {
            com.yunva.video.sdk.interfaces.logic.model.TextMessageNotify notify
                    = new com.yunva.video.sdk.interfaces.logic.model.TextMessageNotify();
            notify.setExpand(textMessageNotify.getExpand());
            notify.setText(textMessageNotify.getText());
            notify.setTroopsId(textMessageNotify.getTroopsId());
            notify.setYunvaId(textMessageNotify.getYunvaId());
            old.onTextMessageNotify(notify);
        }

        @Override
        public void onSendTextMessageResp(long result, String msg, String expand) {
            TextMessageResp resp = new TextMessageResp();
            resp.setExpand(expand);
            resp.setResult(result);
            resp.setMsg(msg);
            old.onSendTextMessageResp(resp);
        }

        @Override
        public void onRealTimeVoiceMessageNotify(String troopsId, long yunvaId, String expand) {
            RealTimeVoiceMessageNotify notify = new RealTimeVoiceMessageNotify();
            notify.setExpand(expand);
            notify.setTroopsId(troopsId);
            notify.setYunvaId(yunvaId);
            old.onRealTimeVoiceMessageNotify(notify);
        }

        @Override
        public void onTroopsModeChangeNotify(RTV.Mode mode, boolean isLeader) {
            TroopsModeChangeNotify notify = new TroopsModeChangeNotify();
            if(mode == RTV.Mode.Free) {
                notify.setModeType(0);
            } else if (mode == RTV.Mode.Robmic) {
                notify.setModeType(1);
            } else {
                notify.setModeType(1);
                notify.setLeader(isLeader);
            }
            old.onTroopsModeChangeNotify(notify);
        }

        @Override
        public void audioRecordUnavailableNotify(int result, String msg) {
            AudioRecordUnavailableNotify notify = new AudioRecordUnavailableNotify();
            notify.setMsg(msg);
            notify.setResult(result);
            old.AudioRecordUnavailableNotify(notify);
        }

        @Override
        public void onAuthResp(long result, String msg, long yunvaId) {

            old.onAuthResp((int)result, msg);

            if(result != 0) {
                LoginResp resp = new LoginResp();
                resp.setResult(result);
                resp.setMsg("帐号授权失败:" + msg);
                old.onLoginResp(resp);
            }
        }

        @Override
        public void onGetRoomResp(long result, String msg) {
            if(result != 0) {
                LoginResp resp = new LoginResp();
                resp.setResult(result);
                resp.setMsg("获取房间信息失败:" + msg);
                old.onLoginResp(resp);
            }
        }

        @Override
        public void onReconnectStart() {
            old.onBeginAutoReLoginWithTryTimes(0);
        }

        @Override
        public void onReconnectFail(int code, String msg) {
            old.onTroopsIsDisconnectNotify();
        }

        @Override
        public void onReconnectSuccess() {

        }
    }
}
