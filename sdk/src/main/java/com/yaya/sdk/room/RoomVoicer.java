package com.yaya.sdk.room;

import android.text.TextUtils;

import com.yaya.sdk.InitParams;
import com.yaya.sdk.MLog;
import com.yaya.sdk.RTV;
import com.yaya.sdk.audio.RecordManager;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.tcp.ITcp;
import com.yaya.sdk.tcp.core.TcpRequest;
import com.yaya.sdk.tcp.TcpResponseCallback;
import com.yaya.sdk.tcp.core.TcpSignalBuilder;
import com.yaya.sdk.tcp.TcpTimeoutCallback;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.LoginResp;
import com.yaya.sdk.tlv.protocol.LogoutResp;
import com.yaya.sdk.tlv.protocol.MicResp;
import com.yaya.sdk.tlv.protocol.ModeSettingReq;
import com.yaya.sdk.tlv.protocol.ModeSettingResp;
import com.yaya.sdk.tlv.protocol.message.TextMessageReq;
import com.yaya.sdk.tlv.protocol.message.TextMessageResp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateExpiredException;
import java.util.concurrent.TimeoutException;

import yaya.tlv.signal.TlvSignal;

/**
 * {@link #enterRoom(RoomTicket)} 请求进入房间
 * {@link #quitRoom()} 请求退出房间
 * {@link #requestMicUp()} 请求上麦
 * {@link #requestMicDown()} 请求下麦
 *
 * {@link #mRoomVoicerWatchDog} Voicer行为的回调
 *
 * Created by ober on 2016/11/10.
 */
public class RoomVoicer {

    private static final String TAG = "RoomVoicer";

    private final String mAppId;
    private final SdkConfig mConfig;
    private final long yunvaId;
    private final String nickname;

    private RoomVoicerWatchDog mRoomVoicerWatchDog;

    private static final long LOGIN_TIMEOUT_DEF = 3000;
    private static final long MIC_UP_TIMEOUT_DEF = 2000;
    private static final long MIC_DOWN_TIMEOUT_DEF = 2000;
    private static final long LOGOUT_TIMEOUT_DEF = 1000;
    private static final long MODE_SETTING_TIMEOUT_DEF = 2000;

    private final long loginTimeout;
    private final long micUpTimeout;
    private final long micDownTimeout;
    private final long logoutTimeout;
    private final long modeSetTimeout;

    private volatile boolean isInRoom;

    private volatile boolean isMicUp;

    private RoomTicket mTicket;

    public RoomVoicer(String appId, SdkConfig config, long yunvaId, String nickname, InitParams params) {
        this.mAppId = appId;
        this.mConfig = config;
        this.yunvaId = yunvaId;
        this.nickname = nickname;
        if(params.getLoginTimeout() != null) {
            loginTimeout = params.getLoginTimeout();
        } else {
            loginTimeout = LOGIN_TIMEOUT_DEF;
        }
        if(params.getLogoutTimeout() != null) {
            logoutTimeout = params.getLogoutTimeout();
        } else {
            logoutTimeout = LOGOUT_TIMEOUT_DEF;
        }
        if(params.getMicUpTimeout() != null) {
            micUpTimeout = params.getMicUpTimeout();
        } else {
            micUpTimeout = MIC_UP_TIMEOUT_DEF;
        }
        if(params.getMicDownTimeout() != null) {
            micDownTimeout = params.getMicDownTimeout();
        } else {
            micDownTimeout = MIC_DOWN_TIMEOUT_DEF;
        }
        if(params.getModeSetTimeout() != null) {
            modeSetTimeout = params.getModeSetTimeout();
        } else {
            modeSetTimeout = MODE_SETTING_TIMEOUT_DEF;
        }
    }

    public synchronized void setWatchDog(RoomVoicerWatchDog watchDog) {
        mRoomVoicerWatchDog = watchDog;
    }

    public synchronized void setExpandForVoice(String expand) {
        this.mTicket.setExpandForVoice(expand);
    }

    //mode 0自由 1抢麦
    public synchronized void enterRoom(RoomTicket ticket) {
        MLog.d(TAG, "enterRoom");

        if(ticket.isUsed()) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.enterRoomCallback(
                        RoomVoicerWatchDog.CODE_BAD_TICKET,
                        new CertificateExpiredException("ticket is used"));
            }
            return;
        }

        if(isInRoom) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.enterRoomCallback(
                        RoomVoicerWatchDog.CODE_ALREADY_IN_ROOM,
                        new UnsupportedOperationException("duplicate enter room"));
            }
            return;
        }

        ITcp tcp = YayaTcp.getInstance();
        tcp.open();

        if(tcp.getConnection() != null && tcp.getConnection().isConnected()) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.enterRoomCallback(
                        RoomVoicerWatchDog.CODE_UNKNOWN_STATE,
                        new IllegalStateException("user not in room, but tcp is connected"));
            }
            return;
        }

        InetSocketAddress addr = new InetSocketAddress(ticket.getTroopsInfo().getHost(),
                ticket.getTroopsInfo().getPort());
        tcp.getConnection().connect(addr);

        if(!tcp.getConnection().isConnected()) {
            //connect fail
            //tcp Exception handled in tcpExceptionHandler
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.enterRoomCallback(RoomVoicerWatchDog.CODE_CONNNECT_FAIL,
                        new SocketException("connect fail"));
            }
            return;
        }

        TlvSignal req = TcpSignalBuilder.with(mAppId, mConfig)
                .buildLoginReq(yunvaId,
                        ticket.getTroopsInfo().getToken(),
                        ticket.getTroopsInfo().getTroopsId(),
                        ticket.getSeq());

        VoicerLoginCallback loginCallback = new VoicerLoginCallback(
                System.currentTimeMillis(),
                ticket);
        TcpRequest tcpRequest = new TcpRequest(req);
        tcpRequest.setTcpResponseCallback(loginCallback);
        tcpRequest.setTcpTimoutCallback(loginCallback);
        YayaTcp.getInstance().sendAndListen(tcpRequest, loginTimeout);

        //ticket is used, expire
        ticket.setUsed();
    }

    public synchronized RoomTicket getTicket() {
        return mTicket;
    }

    public synchronized void sendTextMsg(String msg, final String expand) {
        MLog.d(TAG, "sendTextMsg: " + msg);
        if(TextUtils.isEmpty(msg)) {
            MLog.d(TAG, "empty text msg, ignore it");
            return;
        }

        if(!isInRoom) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.sendTextMsgCallback(
                        RoomVoicerWatchDog.CODE_NOT_IN_ROOM,
                        new UnsupportedOperationException("not in room"), expand);
            }
            return;
        }

        TextMessageReq signal = TcpSignalBuilder.with(mAppId, mConfig)
                .buildTextMsgReq(mTicket.getTroopsInfo().getTroopsId(),
                        yunvaId, msg, expand);
        TcpRequest req = new TcpRequest(signal);
        req.setTcpResponseCallback(new TcpResponseCallback() {
            @Override
            public void onTcpResponse(TlvSignal req, TlvSignal resp) {
                TextMessageResp textMessageResp = (TextMessageResp) resp;
                MLog.d(TAG, "send text msg resp " + textMessageResp);
                if(mRoomVoicerWatchDog != null) {
                    long result = textMessageResp.getResult();
                    if(textMessageResp.getResult() == 0) {
                        mRoomVoicerWatchDog.sendTextMsgCallback(0, null, expand);
                    } else {
                        mRoomVoicerWatchDog.sendTextMsgCallback((int)result,
                                new SocketException(textMessageResp.getMsg()), mTicket.getExpandForVoice());
                    }
                }
            }
        });
        YayaTcp.getInstance().sendAndListen(req);
    }

    public synchronized void requestMicUp() {
        MLog.d(TAG, "requestMicUp");

        if(!isInRoom) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.micUpCallback(
                        RoomVoicerWatchDog.CODE_NOT_IN_ROOM,
                        new UnsupportedOperationException("not in room"));
            }
            return;
        }

        if(isMicUp) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.micUpCallback(
                        RoomVoicerWatchDog.CODE_DUPLICATE_MIC_UP,
                        new UnsupportedOperationException("duplicate mic up"));
            }
            return;
        }
        if(RecordManager.getInstance().isRecording()) {
            RecordManager.getInstance().stopRecord();
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.micUpCallback(
                        RoomVoicerWatchDog.CODE_DUPLICATE_MIC_UP,
                        new UnsupportedOperationException("current is recording"));
            }
            return;
        }
        TlvSignal micSignal = TcpSignalBuilder.with(mAppId, mConfig)
                .buildMicReq(yunvaId, mTicket.getTroopsInfo().getTroopsId(), "1");
        TcpRequest request = new TcpRequest(micSignal);
        request.setTcpTimoutCallback(new TcpTimeoutCallback() {
            @Override
            public void onSignalTimeout(TlvSignal req) {
                MLog.e(TAG, "mic up timeout");
                if(mRoomVoicerWatchDog != null) {
                    Exception e = new SocketTimeoutException("mic up timeout");
                    mRoomVoicerWatchDog.micUpCallback(
                            RoomVoicerWatchDog.CODE_MIC_UP_TIMEOUT, e);
                }
            }
        });
        request.setTcpResponseCallback(new TcpResponseCallback() {
            @Override
            public void onTcpResponse(TlvSignal req, TlvSignal resp) {
                MLog.d(TAG, "mic resp " + resp.toString());
                MicResp micResp = (MicResp) resp;
                if(micResp.getResult() != 0) {
                    MLog.e(TAG, "mic fail code = "
                            + micResp.getResult() + ", msg = " + micResp.getMsg());
                    if(mRoomVoicerWatchDog != null) {
                        mRoomVoicerWatchDog.micUpCallback(RoomVoicerWatchDog.CODE_MIC_UP_ERR_RESP,
                                new IOException(micResp.getMsg()));
                    }
                    return;
                }
                isMicUp = true;
                if(mRoomVoicerWatchDog != null) {
                    mRoomVoicerWatchDog.micUpCallback(RoomVoicerWatchDog.CODE_SUCCESS, null);
                }
                RecordManager.getInstance().startRecord(yunvaId,
                        mTicket.getTroopsInfo().getTroopsId(),
                        mTicket.getExpandForVoice(), mAppId, mConfig,
                        new RecordManager.RecordExceptionHandler() {
                            @Override
                            public void onRecordException(int code) {
                                if(mRoomVoicerWatchDog != null) {
                                    mRoomVoicerWatchDog.recordException(code);
                                }
                            }
                        });
            }
        });
        YayaTcp.getInstance().sendAndListen(request, micUpTimeout);
        MLog.d(TAG, "send mic request");
    }

    public void requestChangeMode(final RTV.Mode mode) {
        if(!isInRoom) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.onModeChangeCallback(RoomVoicerWatchDog.CODE_NOT_IN_ROOM,
                        "not int room", null);
            }
            return;
        }

        byte byteMode;

        if(mode == RTV.Mode.Free) {
            byteMode = 0;
        } else if(mode == RTV.Mode.Robmic) {
            byteMode = 1;
        } else {
            byteMode = 2;
        }

        if(mTicket.getMode() == byteMode) {
            mRoomVoicerWatchDog.onModeChangeCallback(RoomVoicerWatchDog.CODE_SUCCESS,
                    "already in target mode", mode);
            return;
        }

        ModeSettingReq req = TcpSignalBuilder.with(mAppId, mConfig)
                .buildModeSetReq(byteMode, (byte) 0, yunvaId, nickname);
        TcpRequest tcpRequest = new TcpRequest(req);
        tcpRequest.setTcpResponseCallback(new TcpResponseCallback() {
            @Override
            public void onTcpResponse(TlvSignal req, TlvSignal resp) {
                ModeSettingResp modeSettingResp = (ModeSettingResp) resp;
                long code = modeSettingResp.getResult();
                if(modeSettingResp.getResult() == 0) {
                    if(mRoomVoicerWatchDog != null) {
                        mRoomVoicerWatchDog.onModeChangeCallback(RoomVoicerWatchDog.CODE_SUCCESS, "", mode);
                    }
                } else {
                    if(mRoomVoicerWatchDog != null) {
                        mRoomVoicerWatchDog.onModeChangeCallback((int)code, modeSettingResp.getMsg(), mode);
                    }
                }
            }
        });
        YayaTcp.getInstance().sendAndListen(tcpRequest);
        MLog.d(TAG, "send mode setting req:" + byteMode);
    }

    public void requestMicDown() {
        if(!isInRoom) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.micDownCallback(
                        RoomVoicerWatchDog.CODE_NOT_IN_ROOM,
                        new UnsupportedOperationException("not in room"));
            }
            return;
        }
        if(!isMicUp) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.micDownCallback(
                        RoomVoicerWatchDog.CODE_DUPLICATE_MIC_DOWN,
                        new UnsupportedOperationException("duplicated mic down"));
            }
            return;
        }

        TlvSignal micSignal = TcpSignalBuilder.with(mAppId, mConfig)
                .buildMicReq(yunvaId, mTicket.getTroopsInfo().getTroopsId(), "0");
        TcpRequest tcpRequest = new TcpRequest(micSignal);
        tcpRequest.setTcpTimoutCallback(new TcpTimeoutCallback() {
            @Override
            public void onSignalTimeout(TlvSignal req) {
                if(mRoomVoicerWatchDog != null) {
                    mRoomVoicerWatchDog.micDownCallback(
                            RoomVoicerWatchDog.CODE_MIC_DOWN_TIMEOUT,
                            new TimeoutException("mic down time out"));
                }
                RecordManager.getInstance().stopRecord();
                isMicUp = false;
            }
        });
        tcpRequest.setTcpResponseCallback(new TcpResponseCallback() {
            @Override
            public void onTcpResponse(TlvSignal req, TlvSignal resp) {
                MLog.d(TAG, "mic resp " + resp.toString());
                MicResp micResp = (MicResp) resp;
                if(micResp.getResult() != 0) {
                    if(mRoomVoicerWatchDog != null) {
                        mRoomVoicerWatchDog.micDownCallback(
                                RoomVoicerWatchDog.CODE_MIC_DOWN_ERR_RESP,
                                new IOException(micResp.getMsg()));
                    }
                } else {
                    if(mRoomVoicerWatchDog != null) {
                        mRoomVoicerWatchDog.micDownCallback(0 ,null);
                    }
                }

                RecordManager.getInstance().stopRecord();
                isMicUp = false;
            }
        });
        YayaTcp.getInstance().sendAndListen(tcpRequest, micDownTimeout);
    }


    public synchronized boolean isMicUp() {
        return isMicUp;
    }

    public synchronized void quitRoom() {
        MLog.d(TAG, "quitRoom");
        if(!isInRoom) {
            return;
        }
        if(isMicUp) {
            requestMicDown();
        }

        isInRoom = false;
        isMicUp = false;

        if(YayaTcp.getInstance().getConnection() == null
                || !YayaTcp.getInstance().getConnection().isConnected()) {
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.quitRoomCallback(null);
            }
            return;
        }

        TlvSignal logoutSignal = TcpSignalBuilder
                .with(mAppId, mConfig)
                .buildLogoutReq(mTicket.getTroopsInfo().getTroopsId(),
                        yunvaId);
        TcpRequest req = new TcpRequest(logoutSignal);
        VoicerLogoutCallback callback = new VoicerLogoutCallback();
        req.setTcpResponseCallback(callback);
        req.setTcpTimoutCallback(callback);
        YayaTcp.getInstance().sendAndListen(req, logoutTimeout);
    }

    public synchronized boolean isInRoom() {
        return isInRoom;
    }

    private void enterRoomSuccess(RoomTicket ticket) {
        isInRoom = true;
        mTicket = ticket;
        mRoomVoicerWatchDog.enterRoomCallback(RoomVoicerWatchDog.CODE_SUCCESS, null);
    }

    private void enterRoomFail(int code, Exception e) {
        isInRoom = false;
        mRoomVoicerWatchDog.enterRoomCallback(code, e);
    }

    private class VoicerLogoutCallback implements TcpResponseCallback, TcpTimeoutCallback {

        @Override
        public void onTcpResponse(TlvSignal req, TlvSignal resp) {
            MLog.d(TAG, resp.toString());
            doQuitRoom((LogoutResp)resp);
        }

        @Override
        public void onSignalTimeout(TlvSignal req) {
            MLog.w(TAG, "logout request timeout");
            doQuitRoom(null);
        }

        private void doQuitRoom(LogoutResp resp) {
            if(YayaTcp.getInstance().getConnection() != null) {
                YayaTcp.getInstance().getConnection().disconnect();
            }
            if(mRoomVoicerWatchDog != null) {
                mRoomVoicerWatchDog.quitRoomCallback(resp);
            }
        }
    }

    private class VoicerLoginCallback implements TcpResponseCallback, TcpTimeoutCallback {

        long startLoginTime;

        RoomTicket ticket;

        VoicerLoginCallback(long startLoginTime, RoomTicket ticket) {
            this.startLoginTime = startLoginTime;
            this.ticket = ticket;
        }

        @Override
        public void onTcpResponse(TlvSignal req, TlvSignal resp) {
            LoginResp loginResp = (LoginResp) resp;

            long elapsed = System.currentTimeMillis() - startLoginTime;

            MLog.i(TAG, "login response elapsed = " + elapsed);
            MLog.i(TAG, resp.toString());

            if(loginResp.getResult() != 0) {
                long r = loginResp.getResult();
                Exception e = new IOException(loginResp.getMsg());
                enterRoomFail((int)r, e);
                return;
            }

            final byte mode = ticket.getMode();
            if((loginResp.getLeaderMode() != null && loginResp.getLeaderMode() == 1 && mode == 3)||mode == loginResp.getMode()) {
                MLog.d(TAG, "returned mode equals mode in ticket");
                enterRoomSuccess(ticket);
            } else {
                TlvSignal modeSignal = TcpSignalBuilder.with(mAppId, mConfig)
                        .buildModeSetReq(mode, (byte)0, yunvaId, nickname);
                TcpRequest request = new TcpRequest(modeSignal);
                request.setTcpResponseCallback(new TcpResponseCallback() {
                    @Override
                    public void onTcpResponse(TlvSignal req, TlvSignal resp) {
                        MLog.d(TAG, resp.toString());
                        ModeSettingResp modeSettingResp = (ModeSettingResp) resp;
                        long result = modeSettingResp.getResult();
                        if(result != 0) {
                            mRoomVoicerWatchDog.enterRoomCallback(
                                    RoomVoicerWatchDog.CODE_MODE_SET_FAIL,
                                    new IOException("set mode fail:"
                                            + modeSettingResp.getResult() +
                                            "," + modeSettingResp.getMsg()));
                            return;
                        }
                        enterRoomSuccess(ticket);
                    }
                });
                request.setTcpTimoutCallback(new TcpTimeoutCallback() {
                    @Override
                    public void onSignalTimeout(TlvSignal req) {
                        MLog.w(TAG, "ModeSetting time out");
                        enterRoomFail(RoomVoicerWatchDog.CODE_MODE_SET_FAIL,
                                new IOException("set mode time out"));
                    }
                });
                YayaTcp.getInstance().sendAndListen(request, modeSetTimeout);
                MLog.d(TAG, "send ModeChangeRequest");
            }
        }

        @Override
        public void onSignalTimeout(TlvSignal signal) {
            MLog.e(TAG, "login request timeout!");
            enterRoomFail(RoomVoicerWatchDog.CODE_LOGIN_TIMEOUT,
                    new TimeoutException("login request time out"));
        }
    }
}
