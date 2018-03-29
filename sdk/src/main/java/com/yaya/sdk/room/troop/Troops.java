package com.yaya.sdk.room.troop;

import android.content.Context;

import com.yaya.sdk.MLog;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.http.YayaHttp;
import com.yaya.sdk.http.YayaRequestBuilder;
import com.yaya.sdk.http.YayaResponseDecoder;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ober on 2016/10/28.
 */
public class Troops implements ITroops {

    private static final String TAG = "Troops";

    private final SdkConfig config;

    private Call mLastCall;

    private YayaRequestBuilder requestBuilder;

    public static ITroops with(Context context, String appId, SdkConfig config) {
        return new Troops(context, appId, config);
    }

    private Troops(Context context, String appId, SdkConfig config) {
        this.config = config;
        requestBuilder = YayaRequestBuilder.with(context, appId, config);
    }

    @Override
    public void getTroopsInfo(final String seq, String appId, final GetTroopCallback callback) {
        MLog.d(TAG, "getTroopsInfo seq=" + seq + ",appId=" + appId);
        if(seq == null) {
            throw new NullPointerException();
        }
        if(mLastCall != null) {
            mLastCall.cancel();
        }
        final Request req = requestBuilder.buildTroopRequest(seq);
        mLastCall = YayaHttp.getInstance().getOkHttp().newCall(req);
        YayaHttp.getInstance().enqueueWithRetryPolicy(mLastCall, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.e(TAG, "getTroopsInfo err:" + e.getMessage());
                callback.onGetTroopFail(GetTroopCallback.CODE_REQUEST_FAIL,
                        e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MLog.d(TAG, "getTroopsInfo Response");
                GetTroopsInfoResp resp;
                try {
                    resp = YayaResponseDecoder.decodeTroopInfo(response);
                    MLog.d(TAG, resp.toString());
                } catch (Exception e) {
                    MLog.e(TAG, "decode err:" + e.getMessage());
                    callback.onGetTroopFail(GetTroopCallback.CODE_DECODE_FAIL,
                            e.getMessage());
                    return;
                }

                if(resp.getResult() != 0) {
                    MLog.e(TAG, "result=" + resp.getResult() + "," + "msg=" + resp.getMsg());
                    callback.onGetTroopFail(GetTroopCallback.CODE_RESPONSE_ERR,
                            "result=" + resp.getResult() + "," + "msg=" + resp.getMsg());
                    return;
                }

                callback.onGetTroopSuccess(seq, resp);
            }
        });
    }
}
