package com.yaya.sdk.account.auth;

import android.content.Context;

import com.yaya.sdk.MLog;
import com.yaya.sdk.account.AccountAuthCallback;
import com.yaya.sdk.account.AccountState;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.http.YayaResponseDecoder;
import com.yaya.sdk.http.YayaHttp;
import com.yaya.sdk.http.YayaRequestBuilder;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthResp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 第三方授权
 * Created by ober on 2016/10/26.
 */
public final class ThirdAuth {

    private static final String TAG = "ThirdAuth";

    private final SdkConfig config;

    private final YayaRequestBuilder requestBuilder;

    public ThirdAuth(Context c, String appId, SdkConfig config) {
        this.config = config;
        this.requestBuilder = YayaRequestBuilder.with(c, appId, config);
    }

    public void auth(String tt, final AccountAuthCallback callback) {
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        MLog.d(TAG, "auth(" + tt + ")");
        AccountState accountState = AccountState.getInstance();
        if(accountState.getThirdAuthResp() != null) {
            ThirdAuthResp lastResp = accountState.getThirdAuthResp();
            final long recentYunvaId = lastResp.getYunvaId();
            final String recentTT = accountState.getTt();
            final boolean isAuthSuccess = accountState.isAuthSuccess();
            if(recentYunvaId > 0 && recentTT != null && recentTT.equals(tt) && isAuthSuccess) {
                MLog.d(TAG, "bind already success");
                callback.onAuthSuccess();
                return;
            }
        }

        requestGmgc(tt, callback);
    }

    public void auth(long yunvaId, String tt, final AccountAuthCallback callback) {
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        MLog.d(TAG, "auth(" + yunvaId + "," + tt + ")");
        if(yunvaId < 0) {
            throw new IllegalArgumentException("无效的yunvaId");
        }

        AccountState accountState = AccountState.getInstance();
        if(accountState.getThirdAuthResp() != null) {
            ThirdAuthResp lastResp = accountState.getThirdAuthResp();
            final long recentYunvaId = lastResp.getYunvaId();
            final String recentTT = accountState.getTt();
            final boolean isAuthSuccess = accountState.isAuthSuccess();
            if(recentYunvaId == yunvaId && recentTT != null && recentTT.equals(tt) && isAuthSuccess) {
                MLog.d(TAG, "bind already success");
                callback.onAuthSuccess();
                return;
            }
        }

        requestGmgc(tt, callback);
    }

    private Call mLastGmgcCall;
    private void requestGmgc(String tt, final AccountAuthCallback callback) {
        MLog.d(TAG, "requestGmgc");
        if(mLastGmgcCall != null) {
            mLastGmgcCall.cancel();
        }
        OkHttpClient client = YayaHttp.getInstance().getOkHttp();
        final Request req = requestBuilder.buildGmgcRequest(tt);
        mLastGmgcCall = client.newCall(req);
        YayaHttp.getInstance().enqueueWithRetryPolicy(mLastGmgcCall, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.e(TAG, "getGmgc err:" + e.getMessage());
                callback.onAuthFailed(AccountAuthCallback.CODE_GMGC_REQUEST_FAIL,
                        e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MLog.d(TAG, "getGmgc Response");
                GetGmgcUserInfoResp resp = null;
                try {
                    resp = YayaResponseDecoder.decodeGmgc(response);
                } catch (Exception e) {
                    MLog.e(TAG, e.getMessage());
                    callback.onAuthFailed(AccountAuthCallback.CODE_GMGC_DECODE_ERR,
                                "decode error:" + e.getMessage());
                    return;
                }

                if(resp.getResult() != 0) {
                    MLog.e(TAG, "resp " + resp.getResult() + "," + resp.getMsg());
                    callback.onAuthFailed(AccountAuthCallback.CODE_GMGC_RESPONSE_ERR,
                                "result="+ resp.getResult() + "," + "msg=" + resp.getMsg());
                    return;
                }

                thirdAuth(resp, callback);

            }
        });
    }

    private Call mLastThirdAuthCall;

    public void thirdAuth(long yunvaId, final String tt, String thirdId, String thirdUserName, final AccountAuthCallback callback) {
        MLog.d(TAG, "thirdAuth yunvaId=" + yunvaId);
        if(mLastThirdAuthCall != null) {
            mLastThirdAuthCall.cancel();
        }
        final Request thirdAuthReq = requestBuilder.buildThirdAuthRequest(yunvaId, tt, thirdId, thirdUserName);
        mLastThirdAuthCall = YayaHttp.getInstance().getOkHttp().newCall(thirdAuthReq);
        YayaHttp.getInstance().enqueueWithRetryPolicy(mLastThirdAuthCall, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.e(TAG, "thirdAuth err: " + e.getMessage());

                callback.onAuthFailed(AccountAuthCallback.CODE_3AUTH_REQUEST_FAIL,
                        e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MLog.d(TAG, "thirdAuth Response");
                ThirdAuthResp authResp;
                try {
                    authResp = YayaResponseDecoder.decode3Auth(response);
                } catch (Exception e) {
                    MLog.e(TAG, e.getMessage());
                    callback.onAuthFailed(AccountAuthCallback.CODE_3AUTH_DECODE_ERR,
                            e.getMessage());
                    return;
                }

                if(authResp.getResult() != 0) {
                    MLog.e(TAG, "resp " + authResp.getResult() + "," + authResp.getMsg());
                    callback.onAuthFailed(AccountAuthCallback.CODE_3AUTH_RESPONSE_ERR,
                            authResp.getMsg());
                    return;
                }

                AccountState.getInstance().setAuthResp(authResp, tt);
                MLog.i(TAG, "bind success");
                callback.onAuthSuccess();
            }
        });
    }

    private void thirdAuth(final GetGmgcUserInfoResp gmgcResp, final AccountAuthCallback callback) {
        thirdAuth(gmgcResp.getYunvaId(), gmgcResp.getT(),
                gmgcResp.getThirdUserId(), gmgcResp.getThirdUserName(), callback);
    }

}
