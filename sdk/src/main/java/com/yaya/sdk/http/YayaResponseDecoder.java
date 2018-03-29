package com.yaya.sdk.http;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tlv.TlvUtil;
import com.yaya.sdk.tlv.YayaTlvStore;
import com.yaya.sdk.tlv.protocol.info.AuthResp;
import com.yaya.sdk.tlv.protocol.info.GetGmgcUserInfoResp;
import com.yaya.sdk.tlv.protocol.info.GetTroopsInfoResp;
import com.yaya.sdk.tlv.protocol.info.RegisterResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthResp;
import com.yaya.sdk.tlv.protocol.init.GetModelParamResp;
import com.yaya.sdk.util.ExceptionUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Response;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/10/26.
 */
public class YayaResponseDecoder {

    private static final String TAG = "YayaResponseDecoder";

    public static GetGmgcUserInfoResp decodeGmgc(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof GetGmgcUserInfoResp) {
            return (GetGmgcUserInfoResp) signal;
        }
        throw new RuntimeException("GetGmgcUserInfoResp wanted but decoded:" + signal.getClass().getName());
    }

    public static ThirdAuthResp decode3Auth(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof ThirdAuthResp) {
            return (ThirdAuthResp) signal;
        }
        throw new RuntimeException("ThirdAuthResp wanted but decoded:" + signal.getClass().getName());
    }

    public static RegisterResp decodeRegister(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof RegisterResp) {
            return (RegisterResp) signal;
        }
        throw new RuntimeException("RegisterResp wanted but decoded:" + signal.getClass().getName());
    }

    public static AuthResp decodeAuth(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof AuthResp) {
            return (AuthResp) signal;
        }
        throw new RuntimeException("AuthResp wanted but decoded:" + signal.getClass().getName());
    }

    public static GetTroopsInfoResp decodeTroopInfo(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof GetTroopsInfoResp) {
            return (GetTroopsInfoResp) signal;
        }
        throw new RuntimeException("GetTroopsInfoResp wanted but decoded:" + signal.getClass().getName());
    }

    public static GetModelParamResp decodeModelParam(Response response) throws Exception {
        TlvSignal signal = decode(response);
        if(signal instanceof GetModelParamResp) {
            return (GetModelParamResp) signal;
        }
        throw new RuntimeException("GetModelParamResp wanted but decoded:" + signal.getClass().getName());
    }

    private static TlvSignal decode(Response response) throws Exception {
        ResponseEntity entity = collectInfo(response);
        return parseResponse(entity);
    }

    private static TlvSignal parseResponse(ResponseEntity info) throws Exception {
        MLog.d(TAG, "parseResponse module=" + info.moduleId + ",msgCode=" + info.msgCode);
        if(!info.isNotNull()) {
            throw new RuntimeException("不完整的header");
        }
        TlvSignal tlvSignal = TlvUtil.decodeTlvSignal(info.moduleId,
                    info.msgCode, info.responseBody,
                    YayaTlvStore.getInstance());
        if(tlvSignal == null) {
            throw new RuntimeException("tlv decoder returned null");
        }
        tlvSignal.setHeader(TlvUtil.buildHeader(
                info.seqNum, info.moduleId, info.msgCode));
        return tlvSignal;
    }

    private static ResponseEntity collectInfo(Response response) {
        ResponseEntity entity = new ResponseEntity();
        try {
            entity.responseBody = response.body().bytes();
        } catch (IOException e) {
            MLog.e(TAG, e.getMessage());
        }

        Headers headers = response.headers();
        String moduleId = headers.get("moduleId");
        String msgCode = headers.get("msgCode");
        String msgId = headers.get("msgId");
        if(moduleId == null) {
            MLog.w(TAG, "null moduleId");
        } else {
            entity.moduleId = Integer.valueOf(moduleId);
        }
        if(msgCode == null) {
            MLog.w(TAG, "null msgCode");
        } else {
            entity.msgCode = Integer.valueOf(msgCode);
        }
        if(msgId == null) {
            MLog.w(TAG, "null msgId");
        } else {
            entity.seqNum = Long.valueOf(msgId);
        }
        return entity;
    }

    private static final class ResponseEntity {
        byte[] responseBody;
        Integer moduleId;
        Integer msgCode;
        Long seqNum;

        boolean isNotNull(){
            return responseBody != null && null != moduleId&& null != msgCode && null != seqNum;
        }
    }
}
