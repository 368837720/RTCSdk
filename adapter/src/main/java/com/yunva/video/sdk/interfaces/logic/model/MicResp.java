package com.yunva.video.sdk.interfaces.logic.model;


/**
 * Created by wy on 14-4-1.
 */
public class MicResp {
    private Long result;
    private String msg;
    private String actionType;

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("result:").append(result);
        sb.append("|msg:").append(msg);
        sb.append("|actionType:").append(actionType);
        return sb.toString();
    }
}
