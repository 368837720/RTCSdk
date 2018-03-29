package com.yaya.sdk.tlv.protocol.init;

import java.util.ArrayList;
import java.util.List;

import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.signal.TlvSignal;

/**
 * 初始化时获取初始化参数的响应
 * Created by ober on 2016/12/2.
 */
@TlvMsg(moduleId = 0xB300, msgCode = 0x0014)
public class GetModelParamResp extends TlvSignal {
    @TlvSignalField(tag = 200, unsigned = Unsigned.UINT32)
    private Long result;
    @TlvSignalField(tag = 201)
    private String msg;
    @TlvSignalField(tag = 1)
    private List<Param> params;

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetModelParamResp:")
                .append("result:" + result)
                .append("|msg:" + msg)
                .append("|")
                .append(params.toString());
        return sb.toString();
    }
}
