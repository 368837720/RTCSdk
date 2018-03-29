package com.yaya.sdk.http;

import android.content.Context;
import android.os.Build;

import com.yaya.sdk.MLog;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.tlv.TlvUtil;
import com.yaya.sdk.tlv.YayaTlvStore;
import com.yaya.sdk.tlv.protocol.info.AuthReq;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoReq;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoResp;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoReq;
import com.yaya.sdk.tlv.protocol.info.RegisterReq;
import com.yaya.sdk.tlv.protocol.info.RegisterResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthReq;
import com.yaya.sdk.tlv.protocol.init.GetModelParamReq;
import com.yaya.sdk.util.NetworkUtil;
import com.yaya.sdk.util.TelephonyUtil;

import java.util.concurrent.atomic.AtomicLong;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import yaya.tlv.TlvStore;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/10/26.
 */
public class YayaRequestBuilder {

    private static final String TAG = "YayaRequestBuilder";

    private static final AtomicLong reqSeq = new AtomicLong(0);

    private final SdkConfig mConfig;
    private final Context mContext;
    private final TlvStore mTlvStore;
    private final String mAppId;

    public YayaRequestBuilder(SdkConfig config, String appId, Context context) {
        mConfig = config;
        mContext = context;
        mTlvStore = YayaTlvStore.getInstance();
        mAppId = appId;
    }

    public static YayaRequestBuilder with(Context c, String appId, SdkConfig config) {
        return new YayaRequestBuilder(config, appId, c);
    }

    public Request buildGmgcRequest(String tt) {
        GetGmgcUserInfoReq req = new GetGmgcUserInfoReq();
        req.setTt(tt);
        req.setAppId(mAppId);
        req.setImei(TelephonyUtil.getImei(mContext));
        req.setImsi(TelephonyUtil.getImsi(mContext));
        req.setMac(TelephonyUtil.getMac(mContext));
        req.setChannelId(TelephonyUtil.getChannelId(mContext));
        req.setAppVersion(TelephonyUtil.getAppVersion(mContext));
        req.setOsType(TelephonyUtil.getOsType());
        req.setOsVersion(String.valueOf(TelephonyUtil.getSystemVersionCode()));
        req.setSdkAppId(mConfig.getSdkAppId());
        req.setSdkAppVersion(mConfig.getSdkVersion());
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    public Request buildThirdAuthRequest(long yunvaId, String tt, String thirdId, String thirdUserName) {
        ThirdAuthReq req = new ThirdAuthReq();
        req.setYunvaId(yunvaId);
        req.setT(tt);
        req.setThirdId(thirdId);
        req.setThirdUserName(thirdUserName);
        req.setImei(TelephonyUtil.getImei(mContext));
        req.setImsi(TelephonyUtil.getImsi(mContext));
        req.setMac(TelephonyUtil.getMac(mContext));
        req.setAppId(mAppId);
        req.setOsType(TelephonyUtil.getOsType());
        req.setNetworkType("" + NetworkUtil.getNetWorkType(mContext));
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    public Request buildThirdAuthRequest(GetGmgcUserInfoResp gmgc) {
        return buildThirdAuthRequest(gmgc.getYunvaId(), gmgc.getT(),
                gmgc.getThirdUserId(), gmgc.getThirdUserName());
    }

    public Request buildRegisterRequest() {
        RegisterReq req = new RegisterReq();
        req.setAppId(mAppId);
        req.setAppVersion(TelephonyUtil.getAppVersion(mContext));
        req.setChannelId(TelephonyUtil.getChannelId(mContext));
        req.setCpuType(TelephonyUtil.getCpuType());
        req.setDisplay(TelephonyUtil.getDisplay(mContext));
        req.setFactory(TelephonyUtil.getManufacturer());
        req.setImei(TelephonyUtil.getImei(mContext));
        req.setImsi(TelephonyUtil.getImsi(mContext));
        req.setMac(TelephonyUtil.getMac(mContext));
        req.setModel(TelephonyUtil.getTelephonyModel());
        req.setOsType(TelephonyUtil.getOsType());
        req.setOsVersion(String.valueOf(TelephonyUtil.getSystemVersionCode()));
        req.setSdkAppId(mConfig.getSdkAppId());
        req.setSdkVersion(mConfig.getSdkVersion());
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    public Request buildAuthRequest(RegisterResp regResp) {
        AuthReq req = new AuthReq();
        req.setYunvaId(regResp.getYunvaId());
        req.setPassword(regResp.getPassword());
        req.setImei(TelephonyUtil.getImei(mContext));
        req.setImsi(TelephonyUtil.getImsi(mContext));
        req.setMac(TelephonyUtil.getMac(mContext));
        req.setAppId(mAppId);
        req.setOsType(TelephonyUtil.getOsType());
        req.setAppVersion(TelephonyUtil.getAppVersion(mContext));
        req.setNetworkType("" + NetworkUtil.getNetWorkType(mContext));
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    public Request buildTroopRequest(String seq) {
        GetTroopsInfoReq req = new GetTroopsInfoReq();
        req.setAppId(mAppId);
        req.setSeq(seq);
        req.setSdkAppId(mConfig.getSdkAppId());
        req.setSdkAppVersion(mConfig.getSdkVersion());
        req.setImei(TelephonyUtil.getImei(mContext));
        req.setImsi(TelephonyUtil.getImsi(mContext));
        req.setMac(TelephonyUtil.getMac(mContext));
        req.setOsType(TelephonyUtil.getOsType());
        req.setOsVersion(String.valueOf(TelephonyUtil.getSystemVersionCode()));
        //req.setHasVideo("1"); 1表示有视频
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    public Request buildModelParamReq() {
        GetModelParamReq req = new GetModelParamReq();
        req.setAppId(mAppId);
        req.setFactory(Build.MANUFACTURER);
        req.setModel(Build.MODEL);
        req.setSdkAppId(mConfig.getSdkAppId());
        req.setSdkVersion(mConfig.getSdkVersion());
        req.setOsVersion(String.valueOf(Build.VERSION.SDK_INT));
        try {
            return buildTLVReq(req, true);
        } catch (Exception e) {
            MLog.e(TAG, e.getMessage());
            throw new RuntimeException("encode tlv err");
        }
    }

    private Request buildTLVReq(TlvSignal tlv,  boolean isWaitingResponseFlag) throws Exception {
        long seqNum = reqSeq.incrementAndGet();
        int moduleId = TlvUtil.getModuleId(tlv);
        int msgCode = TlvUtil.getMsgCode(tlv);
        tlv.setHeader(TlvUtil.buildHeader(seqNum, moduleId, msgCode));
        RequestBody body = RequestBody.create(
                MediaType.parse("application/octet-stream"),
                TlvUtil.encodeTlvSignalBody(tlv, mTlvStore));
        Request.Builder builder = new Request.Builder();
        builder.addHeader("connection", "close")
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept-Encoding", "identity")
                .addHeader("appId", mAppId)
                .addHeader("moduleId", String.valueOf(moduleId))
                .addHeader("msgCode", String.valueOf(msgCode))
                .addHeader("msgId", String.valueOf(seqNum))
                .addHeader("flag", isWaitingResponseFlag ? "1" : "0")
                .url(mConfig.getAccessServer())
                .post(body);
        return builder.build();
    }

}
