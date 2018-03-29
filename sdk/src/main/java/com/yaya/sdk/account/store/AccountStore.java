package com.yaya.sdk.account.store;

/**
 * 本地保存yunvaId 和 psswd
 * Created by ober on 2016/10/26.
 */
public interface AccountStore {
    /**
     * 读取UserInfo
     *
     * 如果没有 null;
     * 如果有 String[0]=usr String[1]=psswd
     */
    String[] getUserInfo();

    /**
     * 储存UserInfo
     * @param userInfo String[0]=usr String[1]=psswd
     */
    void putUserInfo(String[] userInfo);

    /**
     * 清除UserInfo
     */
    void clearInfo();

    int INDEX_USR = 0;
    int INDEX_PSSWD = 1;
}
