package com.yaya.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.yaya.sdk.account.AccountAuthCallback;
import com.yaya.sdk.account.AccountState;
import com.yaya.sdk.account.auth.ThirdAuth;
import com.yaya.sdk.account.auth.YayaAuth;
import com.yaya.sdk.audio.PlayManager;
import com.yaya.sdk.audio.RecordManager;
import com.yaya.sdk.audio.VoiceMsgRespDispatcher;
import com.yaya.sdk.audio.core.AudioWebrtcTool;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.config.ConfigFactory;
import com.yaya.sdk.http.YayaHttp;
import com.yaya.sdk.http.dns.DnsLoader;
import com.yaya.sdk.modelparam.ParamLoader;
import com.yaya.sdk.modelparam.ParamLoaderImpl;
import com.yaya.sdk.room.ModeNotifyDispatcher;
import com.yaya.sdk.room.RoomTicket;
import com.yaya.sdk.room.RoomVoicer;
import com.yaya.sdk.room.RoomVoicerWatchDog;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.text.TextMessageDispatcher;
import com.yaya.sdk.tlv.YayaTlvStore;
import com.yaya.sdk.tlv.protocol.LogoutResp;
import com.yaya.sdk.tlv.protocol.ModeChangeNotify;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;
import com.yaya.sdk.room.troop.GetTroopCallback;
import com.yaya.sdk.room.troop.ITroops;
import com.yaya.sdk.room.troop.Troops;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

/**
 * Created by ober on 2016/10/26.
 */
public class YayaRTV implements RTV {

    private static final String TAG = "YayaSdk";

    private static YayaRTV yayaRTV;

    private SdkConfig mConfig;

    private String mAppId;

    private YayaAuth mYayaAuth;

    private ThirdAuth mThirdAuth;

    private Context mContext;

    private VideoTroopsRespondListener mResponseListener;

    private RoomVoicer mRoomVoicer;

    private ExceptionRecovery mExceptionRecovery;

    private byte mMode = 0; //语音模式

    private InitParams mInitParams;

    public static RTV getInstance() {
        if(yayaRTV == null) {
            yayaRTV = new YayaRTV();
        }
        return yayaRTV;
    }

    public VideoTroopsRespondListener getResponseListener() {
        return mResponseListener;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void init(final Context context, String appId,
                     final VideoTroopsRespondListener listener, Env env,
                     final InitParams params, Mode mode) {
        if(context == null || TextUtils.isEmpty(appId)) {
            throw new NullPointerException("null context or appId");
        }
        if(params != null) {
            mInitParams = params;
        } else {
            mInitParams = InitParams.getDefault();
        }
        final Context c = context.getApplicationContext();
        this.mConfig = ConfigFactory.buildConfig(c, env);
        this.mAppId = appId;
        mThirdAuth = new ThirdAuth(c, appId, mConfig);
        mYayaAuth = new YayaAuth(c, appId, mConfig);
        mContext = c;
        mResponseListener = listener;
        //this.mMode = mode == Mode.Free ? (byte)0 : 1;
        if(mode == null) mode = Mode.Free;
        if(mode == Mode.Free) {
            mMode = 0;
        } else if(mode == Mode.Robmic) {
            mMode = 1;
        } else {
            mMode = 2;
        }
        Runnable initTask = new Runnable() {
            @Override
            public void run() {
                final InitParams p;
                if(params == null) {
                    p = InitParams.getDefault();
                } else {
                    p = params;
                }
                if(p.getUseDNSCache()) {
                    DnsLoader loader = new DnsLoader(c,
                            p.getCustomDnsMap(),
                            p.getLoadDnsTimeout());
                    loader.loadDnsBackground(mConfig.getAccessServer());
                }
                YayaHttp.getInstance().init(c,
                        p.getHttpTimeout(),
                        p.getUseDNSCache());
                YayaTlvStore.getInstance();
                YayaTcp.getInstance().open();
                loadYayaLibrary(c);
                mExceptionRecovery = YayaExceptionRecovery.newInstance(c,
                        mYayaAuth, mThirdAuth, p.getRetryCount(), p.getRetryTimeout());

                ParamLoader paramLoader = ParamLoaderImpl.create(c, mAppId, mConfig);
                paramLoader.load(new ParamLoader.LoaderCallback() {
                    @Override
                    public void onLoadBack(int success, int param) {
                        MLog.d(TAG, "model param load back:" + success + "," + param);
                        AudioWebrtcTool.echoServiceCreart(100);
                        RecordManager.getInstance().openLib();
                        PlayManager.getInstance().openLib();
                        listener.initComplete();
                    }
                });

            }
        };
        if(params != null && !params.isInitMethodAsynchronous()) {
            initTask.run();
        } else {
            new Thread(initTask, "InitSdkThread").start();
        }
    }

    @Override
    public void init(final Context context, String appId,
                     final VideoTroopsRespondListener listener, Env env, Mode mode) {
        init(context, appId, listener, env, InitParams.getDefault(), mode);
    }

    @Override
    public void setNetStateListener(YayaNetStateListener listener) {
        if(YayaTcp.getInstance().getConnection() != null) {
            YayaTcp.getInstance().getConnection().setHeartBeatCallback(listener);
        }
    }

    @Override
    public void destroy() {
        if(mExceptionRecovery != null) {
            mExceptionRecovery.cancel();
        }
        RecordManager.getInstance().stopRecord();
        PlayManager.getInstance().stopListenAndPlay();
        if(mRoomVoicer != null) {
            mRoomVoicer.quitRoom();
            mRoomVoicer = null;
        }
        YayaTcp.getInstance().close();
        RecordManager.getInstance().destroy();
        PlayManager.getInstance().destroy();
        YayaRTV.yayaRTV = null;
    }

    @Override
    public String getSdkVersion() {
        return mConfig.getSdkVersion();
    }

    @Override
    public String getAppId() {
        return mAppId;
    }

    @Override
    public void loginBinding(String tt, String seq) {
        if(TextUtils.isEmpty(seq)) {
            throw new NullPointerException("null seq");
        }
        AccountAuthCallback callback = new LocalAuthCallback(seq);
        if(mExceptionRecovery != null) {
            mExceptionRecovery.prepare(seq, new LocalReconnectCallback(), callback);
        }
        mThirdAuth.auth(tt, callback);
    }

    @Override
    public void thirdAuth(long yunvaId, String tt, String thirdId, String thirdUserName, String seq) {
        if(TextUtils.isEmpty(seq)) {
            throw new NullPointerException("null seq");
        }
        AccountAuthCallback callback = new LocalAuthCallback(seq);
        if(mExceptionRecovery != null) {
            mExceptionRecovery.prepare(seq, new LocalReconnectCallback(), callback);
        }
        mThirdAuth.thirdAuth(yunvaId, tt, thirdId, thirdUserName, callback);
    }

    @Override
    public void login(String seq) {
        if(TextUtils.isEmpty(seq)) {
            throw new NullPointerException("null seq");
        }
        AccountAuthCallback callback = new LocalAuthCallback(seq);
        if(mExceptionRecovery != null) {
            mExceptionRecovery.prepare(seq, new LocalReconnectCallback(), callback);
        }
        mYayaAuth.auth(callback);
    }

    @Override
    public void modeSettingReq(Mode mode) {
        if(mode == null) {
            mode = Mode.Free;
        }
        MLog.d(TAG, "modeSettingReq(" + mode.name() + ")");

        final Long yunvaId = AccountState.getInstance().getYunvaId();
        if(yunvaId == null) {
            MLog.e(TAG, "mode set fail without login");
            mResponseListener.onModeSettingResp(-1, "mode set fail without login", mode);
            return;
        }

        if(mRoomVoicer == null || !mRoomVoicer.isInRoom()) {
            MLog.e(TAG, "not in room");
            mResponseListener.onModeSettingResp(-1, "mode set fail not in room", mode);
            return;
        }

        mRoomVoicer.requestChangeMode(mode);
    }

    @Override
    public void login(long yunvaId, String password, String seq) {
        if(TextUtils.isEmpty(seq)) {
            throw new NullPointerException("null seq");
        }
        AccountAuthCallback callback = new LocalAuthCallback(seq);
        if(mExceptionRecovery != null) {
            mExceptionRecovery.prepare(seq, new LocalReconnectCallback(), callback);
        }
        mYayaAuth.auth(yunvaId, password, callback);
    }

    @Override
    public void logout() {
        MLog.d(TAG, "logout");
        Long yunvaId = AccountState.getInstance().getYunvaId();
        if(yunvaId == null) {
            MLog.e(TAG, "found null yunvaId, has login before?");
            if(mResponseListener != null) {
                mResponseListener.onLogoutResp(-1, "null yunvaId");
            }
            return;
        }

        RecordManager.getInstance().stopRecord();

        if(mRoomVoicer == null || !mRoomVoicer.isInRoom()) {
            MLog.w(TAG, "no room info found");
            if(mResponseListener != null) {
                mResponseListener.onLogoutResp(-2, "null room info");
            }
            return;
        }

        mRoomVoicer.quitRoom();
    }

    @Override
    public void mic(final String actionType, final String expand) {
        MLog.d(TAG, "mic(" + actionType + "), expand = " + expand);
        cancelLastMicDeadline();
        final Long yunvaId = AccountState.getInstance().getYunvaId();
        if(yunvaId == null) {
            MLog.e(TAG, "mic fail without login");
            mResponseListener.onMicResp(-1, "mic fail without login", actionType);
            return;
        }

        if(mRoomVoicer == null || !mRoomVoicer.isInRoom()) {
            MLog.e(TAG, "not in room");
            mResponseListener.onMicResp(-1, "mic fail not in room", actionType);
            return;
        }

        mRoomVoicer.setExpandForVoice(expand);

        if(actionType.equals("1")) {
            mRoomVoicer.requestMicUp();
        } else if(actionType.equals("0")) {
            mRoomVoicer.requestMicDown();
        }
    }

    @Override
    public void mic(String actionType) {
        mic(actionType, null);
    }

    @Override
    public void micUp(String expand) {
        mic(ACTION_TYPE_OPEN_MIC, expand);
    }

    @Override
    public void micUp() {
        mic(ACTION_TYPE_OPEN_MIC);
    }

    private MicDeadLineTask mLastMicDeadLineTask;
    private Handler mLastMicHandler;

    private void cancelLastMicDeadline() {
        if(mLastMicHandler != null && mLastMicDeadLineTask != null) {
            mLastMicDeadLineTask.cancel();
            mLastMicHandler.removeCallbacks(mLastMicDeadLineTask);
        }
    }

    @Override
    public void micUpWithLimit(long limitMillis, String expand) {
        MLog.d(TAG, "micUpWithLimit limitMills=" + limitMillis + ",expand=" + expand);
        micUp(expand);
        mLastMicDeadLineTask = new MicDeadLineTask();
        Looper yayaLooper = YayaTcp.getInstance().getYayaThreadLooper();
        mLastMicHandler = new Handler(yayaLooper);
        mLastMicHandler.postDelayed(mLastMicDeadLineTask, limitMillis + 500);
    }

    private class MicDeadLineTask implements Runnable {

        private final Object lock = new Object();

        boolean isCanceled;

        void cancel() {
            synchronized (lock) {
                isCanceled = true;
            }
        }

        @Override
        public void run() {

            synchronized (lock) {
                if(isCanceled) {
                    return;
                }
            }

            MLog.d(TAG, "mic deadline run()");
            if(mRoomVoicer == null) {
                MLog.d(TAG, "null room");
                return;
            }
            if(!mRoomVoicer.isInRoom()) {
                MLog.d(TAG, "not in room");
                return;
            }
            if(!mRoomVoicer.isMicUp()) {
                MLog.d(TAG, "current is mic down");
                return;
            }
            mRoomVoicer.requestMicDown();
        }
    }

    @Override
    public void micUpWithLimit(long limitMillis) {
        micUpWithLimit(limitMillis, null);
    }

    @Override
    public void micDown(String expand) {
        mic(ACTION_TYPE_CLOSE_MIC, expand);
    }

    @Override
    public void micDown() {
        micDown(null);
    }

    @Override
    public void sendTextMessage(String text) {
        sendTextMessage(text, null);
    }

    @Override
    public void sendTextMessage(String text, String expand) {
        MLog.d(TAG, "sendTextMessage text=" + text + ",expand=" + expand);
        final Long yunvaId = AccountState.getInstance().getYunvaId();
        if(yunvaId == null) {
            MLog.e(TAG, "send text fail without login");
            mResponseListener.onSendTextMessageResp(-1, "mic fail without login", expand);
            return;
        }

        if(mRoomVoicer == null || !mRoomVoicer.isInRoom()) {
            MLog.e(TAG, "not in room");
            mResponseListener.onMicResp(-2, "send text fail not in room", expand);
        }

        mRoomVoicer.sendTextMsg(text, expand);
    }

    @Override
    public boolean setMessageFilter(MessageFilter filter) {
        MLog.d(TAG, "setMessageFilter " + (filter == null ?
                "null" : filter.getClass().getSimpleName()));
        final Long yunvaId = AccountState.getInstance().getYunvaId();
        if(yunvaId == null) {
            MLog.e(TAG, "setMessageFilter fail without login");
            return false;
        }

        if(mRoomVoicer == null || !mRoomVoicer.isInRoom()) {
            MLog.e(TAG, "not in room");
            return false;
        }

        YayaTcp.getInstance().setMessageFilter(filter);
        return true;
    }

    private class LocalAuthCallback implements AccountAuthCallback {

        private String seq;

        LocalAuthCallback(String seq) {
            this.seq = seq;
        }

        @Override
        public void onAuthSuccess() {
            if(mResponseListener != null) {
                mResponseListener.onAuthResp(0, null,
                        AccountState.getInstance().getYunvaId());
            }
            ITroops troops = Troops.with(mContext, mAppId, mConfig);
            troops.getTroopsInfo(seq, mAppId, new LocalGetTroopsCallback());
        }

        @Override
        public void onAuthFailed(final int code, final String msg) {
            if(mResponseListener != null) {
                mResponseListener.onAuthResp(code, msg, -1);
            }
            if(mExceptionRecovery != null) {
                mExceptionRecovery.notifyEnterRoomResult(code, new Exception(msg));
            }
        }
    }

    private class LocalGetTroopsCallback implements GetTroopCallback {

        @Override
        public void onGetTroopSuccess(String seq, GetTroopsInfoResp resp) {
            MLog.d("LocalGetTroopsCallback", "result = " + resp.getResult());
            if(mResponseListener != null) {
                mResponseListener.onGetRoomResp(0, null);
            }
            long yunvaId = AccountState.getInstance().getYunvaId();
            String nickname = AccountState.getInstance().getNickname();
            mRoomVoicer = new RoomVoicer(mAppId, mConfig, yunvaId, nickname, mInitParams);
            mRoomVoicer.setWatchDog(new LocalRoomVoicerWatchDog(seq, resp, mMode));
            RoomTicket ticket = new RoomTicket(resp, mMode, seq);
            mRoomVoicer.enterRoom(ticket);
        }

        @Override
        public void onGetTroopFail(int code, String msg) {
            MLog.w("LocalGetTroopsCallback", "result = " + code + ",msg = " + msg);
            if(mResponseListener != null) {
                mResponseListener.onGetRoomResp(code, msg);
            }
            if(mExceptionRecovery != null) {
                mExceptionRecovery.notifyEnterRoomResult(code, new Exception(msg));
            }
        }
    }

    private void loadYayaLibrary(Context context){
        if (context == null) {
            MLog.e(TAG, "loadYayaLibrary context is null");
            return;
        }
        String path = context.getApplicationInfo().dataDir + "/lib";
        try {
            System.loadLibrary("yayax");
        } catch (UnsatisfiedLinkError e) {
            try {
                System.load(path + "/libyayax.so");
            } catch (Exception e1) {
            }
        }
    }

    private class LocalRoomVoicerWatchDog implements RoomVoicerWatchDog {

        private final String seq;
        private final GetTroopsInfoResp troopsInfo;
        private final byte mode;

        volatile boolean isRetryedEnterRoom = false;

        private ResponseDispatcher mTextDispatcher;
        private ResponseDispatcher mModeNotifyDispatcher;

        LocalRoomVoicerWatchDog(String seq, GetTroopsInfoResp resp, byte mode) {
            this.seq = seq;
            this.troopsInfo = resp;
            this.mode = mode;
            mTextDispatcher = TextMessageDispatcher.create();
            mModeNotifyDispatcher = ModeNotifyDispatcher.create();
        }

        @Override
        public void enterRoomCallback(int code, Exception e) {

            if(code != 0) {
                MLog.w(TAG, "login room fail code=" + code + ","
                        + e.getClass().getSimpleName() + ":" + e.getMessage());
                if(YayaTcp.getInstance().getConnection() != null &&
                        YayaTcp.getInstance().getConnection().isConnected()) {
                    MLog.w(TAG, "disconnect");
                    YayaTcp.getInstance().getConnection().disconnect();
                }
                if(isRetryedEnterRoom) {
                    MLog.e(TAG, "retry fail,call back onLoginResp");
                    mResponseListener.onLoginResp(code, e.getMessage(), -1, (byte)-1);
                    mExceptionRecovery.notifyEnterRoomResult(code, e);
                    isRetryedEnterRoom = false;
                    return;
                }
                MLog.i(TAG, "login will be retry after 2 seconds");
                isRetryedEnterRoom = true;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                RoomTicket ticket = new RoomTicket(troopsInfo, mode, seq);
                mRoomVoicer.enterRoom(ticket);
                return;
            }

            YayaTcp.getInstance().registerSignalDispatcher(ModeChangeNotify.class,
                    mModeNotifyDispatcher);

            PlayManager.getInstance().startListenAndPlay(new LocalPlayCallback());
            YayaTcp.getInstance().registerSignalDispatcher(TextMessageNotify.class,
                    mTextDispatcher);
            if(mResponseListener != null) {
                mResponseListener.onLoginResp(0,
                        null,
                        AccountState.getInstance().getYunvaId(),
                        mRoomVoicer.getTicket().getMode());
            }
            mExceptionRecovery.notifyEnterRoomResult(code, e);
        }

        @Override
        public void quitRoomCallback(LogoutResp resp) {
            if(mExceptionRecovery != null) {
                //退出房间时 取消重连机制
                mExceptionRecovery.cancel();
            }
            YayaTcp.getInstance().setMessageFilter(null);
            YayaTcp.getInstance().unregisterSignalDispatcher(ModeChangeNotify.class);
            YayaTcp.getInstance().unregisterSignalDispatcher(TextMessageNotify.class);
            PlayManager.getInstance().stopListenAndPlay();
            if(resp != null) {
                if(mResponseListener != null) {
                    mResponseListener.onLogoutResp(resp.getResult(), resp.getMsg());
                }
            } else {
                mResponseListener.onLogoutResp(-4, "timeout");
            }
        }

        @Override
        public void micUpCallback(int code, Exception e) {
            if(mResponseListener != null) {
                String msg = e == null ? null : e.getMessage();
                mResponseListener.onMicResp(code, msg, ACTION_TYPE_OPEN_MIC);
            }
            if(code == CODE_MIC_UP_ERR_RESP) {
                //response返回错误码
                //如上麦人数达到最大限制、抢麦模式抢麦失败等 不需重连
                return;
            }
            if(code == CODE_DUPLICATE_MIC_UP) {
                //todo 重复的上麦请求
                //暂不处理
                return;
            }
            if(code != 0) {
                //重连
                if(mExceptionRecovery != null) {
                    mExceptionRecovery.reconnect(code, e == null ? "" : e.getMessage());
                }
            }
        }

        @Override
        public void micDownCallback(int code, Exception e) {
            if(mResponseListener != null) {
                String msg = e == null ? null : e.getMessage();
                mResponseListener.onMicResp(code, msg, ACTION_TYPE_CLOSE_MIC);
            }
            if(code == CODE_MIC_DOWN_ERR_RESP) {
                //请求成功，但服务器返回错误码
            }
            if(code == CODE_DUPLICATE_MIC_DOWN) {
                //重复的下麦请求
                //todo　暂未处理
                return;
            }
            if(code != 0) {
                //重连
                if(mExceptionRecovery != null) {
                    mExceptionRecovery.reconnect(code, e == null ? "" : e.getMessage());
                }
            }
        }

        @Override
        public void sendTextMsgCallback(int code, Exception e, String expand) {
            if(mResponseListener != null) {
                String msg = e == null ? null : e.getMessage();
                mResponseListener.onSendTextMessageResp(code, msg, expand);
            }
        }

        @Override
        public void recordException(int recordErrCode) {
            if(mResponseListener != null){
                mResponseListener.audioRecordUnavailableNotify(recordErrCode, "");
            }
        }

        @Override
        public void onModeChangeCallback(int code, String msg, Mode mode) {
            if(mResponseListener != null) {
                mResponseListener.onModeSettingResp((long)code, msg, mode);
            }
        }
    }

    private class LocalPlayCallback implements
            VoiceMsgRespDispatcher.VoiceMsgDispatchCallback {

        private int blockCount = 0;
        private Handler mYayaThreadHandler;

        LocalPlayCallback() {
            Looper looper = YayaTcp.getInstance().getYayaThreadLooper();
            mYayaThreadHandler = new Handler(looper);
        }

        @Override
        public void onVoiceMsgDispatch(final long yunvaId, final String troopsId, final String expand) {
            //回调cp的listener, 比较频繁，抛到yayaThread
            mYayaThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mResponseListener != null) {
                        mResponseListener.onRealTimeVoiceMessageNotify(troopsId,
                                yunvaId, expand);
                    }
                }
            });
        }

        @Override
        public void onVoiceCacheBlocked() {
            //异常状态
            MLog.w(TAG, "LocalPlayCallback: onVoiceCacheBlocked..");
            if(blockCount >= 20) {
                PlayManager.getInstance().reset();
                blockCount = 0;
            } else {
                blockCount ++;
            }

        }

        @Override
        public void onSendResponseDispatch(final long code, final String msg) {
            mYayaThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mResponseListener != null) {
                        mResponseListener.onSendRealTimeVoiceMessageResp(code, msg);
                    }
                }
            });
        }
    }

    private class LocalReconnectCallback implements ExceptionRecovery.RetryCallback {

        @Override
        public void onReconnectStart() {
            if(mResponseListener != null) {
                mResponseListener.onReconnectStart();
            }
        }

        @Override
        public void onReconnectFail(int errCode, String msg) {
            if(mResponseListener != null) {
                mResponseListener.onReconnectFail(errCode, msg);
            }
        }

        @Override
        public void onReconnectSuccess() {
            if(mResponseListener != null) {
                mResponseListener.onReconnectSuccess();
            }
        }
    }

}
