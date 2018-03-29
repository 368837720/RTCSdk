package com.yaya.sdk.account;

import com.yaya.sdk.tlv.protocol.info.AuthResp;
import com.yaya.sdk.tlv.protocol.info.ThirdAuthResp;

/**
 * 当前账户状态
 * Created by ober on 2016/10/26.
 */
public class AccountState {

    //是否已经auth成功
    private boolean isAuthSuccess;

    private String tt;
    private ThirdAuthResp thirdAuthResp;

    private String psswd;
    private AuthResp authResp;

    private static AccountState instance_;

    private AccountState() {}

    public static AccountState getInstance() {
        if(instance_ == null) {
            instance_ = new AccountState();
        }
        return instance_;
    }

    public AuthResp getAuthResp() {
        return authResp;
    }

    public ThirdAuthResp getThirdAuthResp() {
        return thirdAuthResp;
    }

    public String getTt() {
        return tt;
    }

    public String getPsswd() {
        return psswd;
    }

    public boolean isAuthSuccess() {
        return isAuthSuccess;
    }

    public void setAuthResp(AuthResp authResp, String psswd) {
        thirdAuthResp = null;
        tt = null;
        this.authResp = authResp;
        this.psswd = psswd;
        isAuthSuccess = true;
    }

    public void setAuthResp(ThirdAuthResp thirdAuthResp, String tt) {
        authResp = null;
        psswd = null;
        this.thirdAuthResp = thirdAuthResp;
        this.tt = tt;
        isAuthSuccess = true;
    }

    public Long getYunvaId() {
        if(thirdAuthResp != null) {
            return thirdAuthResp.getYunvaId();
        }
        if(authResp != null) {
            return authResp.getYunvaId();
        }
        return null;
    }

    public String getNickname() {
        if(thirdAuthResp != null) {
            return thirdAuthResp.getNickName();
        }
        if(authResp != null) {
            return authResp.getNickName();
        }
        return null;
    }

    public void reset() {
        tt = null;
        thirdAuthResp = null;
        psswd = null;
        authResp = null;
        isAuthSuccess = false;
    }

}
