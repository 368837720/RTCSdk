package com.yaya.sdk.account.auth;

import android.content.Context;
import android.text.TextUtils;

import com.yaya.sdk.MLog;
import com.yaya.sdk.account.AccountAuthCallback;
import com.yaya.sdk.account.AccountState;
import com.yaya.sdk.account.store.AccountStore;
import com.yaya.sdk.account.store.YayaAccountStore;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.http.YayaHttp;
import com.yaya.sdk.http.YayaRequestBuilder;
import com.yaya.sdk.http.YayaResponseDecoder;
import com.yaya.sdk.tlv.protocol.info.AuthResp;
import com.yaya.sdk.tlv.protocol.info.RegisterResp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Yaya授权
 * Created by ober on 2016/10/26.
 */
public class YayaAuth {

    private static final String TAG = "YayaAuth";

    private final SdkConfig config;
    private final AccountStore accountStore;
    private final YayaRequestBuilder requestBuilder;

    public YayaAuth(Context context, String appId, SdkConfig config) {
        this.config = config;
        this.accountStore = YayaAccountStore.with(config);
        this.requestBuilder = YayaRequestBuilder.with(context, appId, config);
    }

    public void authOnlyLocalStoreExist(final AccountAuthCallback callback) {
        MLog.d(TAG, "authOnlyLocalStoreExist");
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        String[] userInfo = accountStore.getUserInfo();
        if(userInfo == null) {
            callback.onAuthFailed(AccountAuthCallback.CODE_AUTH_NO_STORE, "null userInfo");
            return;
        }
        long yunvaId;
        try {
            yunvaId = Long.valueOf(userInfo[0]);
        } catch (Exception e) {
            MLog.w(TAG, "bad yunvaId:" + userInfo[0]);
            accountStore.clearInfo();
            callback.onAuthFailed(AccountAuthCallback.CODE_AUTH_NO_STORE, "bad yunvaId");
            return;
        }
        AccountAuthCallback wrapper = new AccountAuthCallback() {
            @Override
            public void onAuthSuccess() {
                callback.onAuthSuccess();
            }

            @Override
            public void onAuthFailed(int code, String msg) {
                accountStore.clearInfo();
                callback.onAuthFailed(code, msg);
            }
        };
        auth(yunvaId, userInfo[1], wrapper);
    }

    public void authWithLocalStore(long defYunvaId, String defPsswd, final AccountAuthCallback callback) {
        MLog.d(TAG, "authWithLocalStore");
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        String[] userInfo = accountStore.getUserInfo();
        if(userInfo == null) {
            auth(defYunvaId, defPsswd, callback);
            return;
        }
        long yunvaId;
        try {
            yunvaId = Long.valueOf(userInfo[0]);
        } catch (Exception e) {
            MLog.w(TAG, "bad yunvaId:" + userInfo[0]);
            accountStore.clearInfo();
            auth(defYunvaId, defPsswd, callback);
            return;
        }
        AccountAuthCallback wrapper = new AccountAuthCallback() {
            @Override
            public void onAuthSuccess() {
                callback.onAuthSuccess();
            }

            @Override
            public void onAuthFailed(int code, String msg) {
                accountStore.clearInfo();
                callback.onAuthFailed(code, msg);
            }
        };
        auth(yunvaId, userInfo[1], wrapper);
    }

    public void auth(AccountAuthCallback callback) {
        MLog.d(TAG, "auth()");
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        String[] userInfo = accountStore.getUserInfo();
        if(userInfo != null) {
            auth(Long.valueOf(userInfo[0]), userInfo[1], callback);
            return;
        }
        register(callback);
    }

    public void auth(long yunvaId, String psswd, AccountAuthCallback callback) {
        MLog.d(TAG, "auth(" + yunvaId + "," + psswd + ")");
        if(callback == null) {
            throw new NullPointerException("null callback");
        }
        if(yunvaId < 0) {
            throw new IllegalArgumentException("无效的yunvaId");
        }
        if(TextUtils.isEmpty(psswd)) {
            throw new IllegalArgumentException("空的psswd");
        }
        final AccountState accountState = AccountState.getInstance();
        if(accountState.getAuthResp() != null) {
            final AuthResp recentAuthResp = accountState.getAuthResp();
            final long recentYunvaId = recentAuthResp.getYunvaId();
            final String recentPsswd = accountState.getPsswd();
            final boolean recentSuccess = accountState.isAuthSuccess();
            if(recentSuccess && recentYunvaId == yunvaId
                    && recentPsswd != null && recentPsswd.equals(psswd)) {
                MLog.d(TAG, "login already success");
                callback.onAuthSuccess();
                return;
            }
        }

        register(callback);
    }

    private Call mLastRegisterCall;
    private void register(final AccountAuthCallback callback) {
        MLog.d(TAG, "register");
        if(mLastRegisterCall != null) {
            mLastRegisterCall.cancel();
        }
        Request regReq = requestBuilder
                .buildRegisterRequest();
        mLastRegisterCall = YayaHttp.getInstance().getOkHttp().newCall(regReq);
        YayaHttp.getInstance().enqueueWithRetryPolicy(mLastRegisterCall, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.e(TAG, "register err:" + e.getMessage());
                callback.onAuthFailed(AccountAuthCallback.CODE_REGISTER_REQUEST_FAIL,
                        e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MLog.d(TAG, "register Response");
                RegisterResp registerResp;
                try {
                    registerResp = YayaResponseDecoder.decodeRegister(response);
                } catch (Exception e) {
                    MLog.e(TAG, e.getMessage());
                    callback.onAuthFailed(AccountAuthCallback.CODE_REGISTER_DECODE_ERR,
                            "decode err:" + e.getMessage());
                    return;
                }
                if(registerResp.getResult() != 0) {
                    MLog.e(TAG, "resp " + registerResp.getResult() + "," + registerResp.getMsg());
                    callback.onAuthFailed(AccountAuthCallback.CODE_REGISTER_RESPONSE_ERR,
                            "result="+ registerResp.getResult() +
                                    "," + "msg=" + registerResp.getMsg());
                    return;
                }

                auth(registerResp, callback);
            }
        });
    }

    private Call mLastAuthCall;
    private void auth(final RegisterResp resp, final AccountAuthCallback callback) {
        MLog.d(TAG, "auth " + resp.getYunvaId());
        if(mLastAuthCall != null) {
            mLastAuthCall.cancel();
        }
        final Request authReq = requestBuilder
                .buildAuthRequest(resp);
        mLastAuthCall = YayaHttp.getInstance()
                .getOkHttp()
                .newCall(authReq);
        YayaHttp.getInstance().enqueueWithRetryPolicy(mLastAuthCall, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.e(TAG, "auth err " + e.getClass().getSimpleName() + ":" + e.getMessage());
                callback.onAuthFailed(AccountAuthCallback.CODE_AUTH_REQUEST_FAIL,
                        e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MLog.d(TAG, "auth Response");
                AuthResp authResp;
                try {
                    authResp = YayaResponseDecoder.decodeAuth(response);
                } catch (Exception e) {
                    MLog.e(TAG, e.getMessage());
                    callback.onAuthFailed(AccountAuthCallback.CODE_AUTH_DECODE_ERR,
                            "decode err:" + e.getMessage());
                    return;
                }

                if(authResp.getResult() != 0) {
                    MLog.e(TAG, "resp " + authResp.getResult() + "," + authResp.getMsg());
                    callback.onAuthFailed(AccountAuthCallback.CODE_AUTH_RESPONSE_ERR,
                            "result="+ authResp.getResult() +
                                    "," + "msg=" + authResp.getMsg());
                    return;
                }

                AccountState.getInstance().setAuthResp(authResp, resp.getPassword());
                accountStore.putUserInfo(new String[] {
                        String.valueOf(authResp.getYunvaId()),
                        resp.getPassword()});
                MLog.i(TAG, "auth success");
                callback.onAuthSuccess();
            }
        });
    }

}
