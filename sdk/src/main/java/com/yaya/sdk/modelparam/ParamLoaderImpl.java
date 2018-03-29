package com.yaya.sdk.modelparam;

import android.content.Context;
import android.os.Build;

import com.yaya.sdk.MLog;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.http.YayaRequestBuilder;
import com.yaya.sdk.http.YayaResponseDecoder;
import com.yaya.sdk.tlv.protocol.init.GetModelParamResp;
import com.yaya.sdk.tlv.protocol.init.Param;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ober on 2016/12/2.
 */
public class ParamLoaderImpl extends OkHttpDelayParamLoader {

    private static final String TAG = "HttpModelParamLoader";

    private final String mAppId;
    private final SdkConfig mConfig;
    private final Context mContext;

    public static ParamLoader create(Context c, String appId, SdkConfig config) {
        return new ParamLoaderImpl(c, appId, config);
    }

    ParamLoaderImpl(Context c, String appId, SdkConfig config) {
        super(PrefDelayParamStore.create(c));
        mAppId = appId;
        mConfig = config;
        mContext = c;
    }

    @Override
    Request createReq() {
        MLog.d(TAG, "create Req " + Build.MANUFACTURER + "|" + Build.MODEL);
        return YayaRequestBuilder.with(mContext, mAppId, mConfig)
                .buildModelParamReq();
    }

    @Override
    int resolveResp(Response response) {
        try {
            GetModelParamResp rsp = YayaResponseDecoder.decodeModelParam(response);
            MLog.d(TAG, rsp.toString());
            Long rspResult = rsp.getResult();
            if(rspResult == null) {
                MLog.w(TAG, "resp result=null");
                return DELAY_PARAM_DEF;
            }
            if(rspResult != 0) {
                MLog.i(TAG, "resp result " + rspResult);
                return DELAY_PARAM_DEF;
            }
            List<Param> list = rsp.getParams();
            if(list == null || list.isEmpty()) {
                MLog.i(TAG, "empty list");
                return DELAY_PARAM_DEF;
            }
            Param param = list.get(0);
            String val = param.getValue();
            try {
                int p = Integer.valueOf(val);
                String k = param.getKey();
                MLog.d(TAG, "<k,p>= " + k + "," + p);
                return p;
            } catch (Exception e) {
                MLog.w(TAG, "can not resolve param " + val);
                return DELAY_PARAM_DEF;
            }
        } catch (Exception e) {
            MLog.w(TAG, e.getClass().getSimpleName() + ":" + e.getMessage());
            return DELAY_PARAM_DEF;
        }
    }
}
