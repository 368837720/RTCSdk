package com.yaya.sdk.tlv.protocol.init;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * 初始化时获取初始化参数的请求
 * Created by ober on 2016/12/2.
 */
@TlvMsg(moduleId = 0xB300, msgCode = 0x0013)
public class GetModelParamReq extends TlvSignal {
    @TlvSignalField(tag = 1)
    private String appId;
    @TlvSignalField(tag = 2, unsigned = Unsigned.UINT32)
    private Long yunvaId;
    @TlvSignalField(tag = 3)
    private String factory;
    @TlvSignalField(tag = 4)
    private String model;
    @TlvSignalField(tag = 5)
    private String osType = "1"; //1为android 2为ios
    @TlvSignalField(tag = 6)
    private String osVersion; //操作系统能够
    @TlvSignalField(tag = 7)
    private String reqType = "voice"; //业务类型
    @TlvSignalField(tag = 8)
    private String sdkAppId;
    @TlvSignalField(tag = 9)
    private String sdkVersion;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getYunvaId() {
        return yunvaId;
    }

    public void setYunvaId(Long yunvaId) {
        this.yunvaId = yunvaId;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
}
