package com.yaya.sdk.http.dns;

import android.content.Context;
import android.content.SharedPreferences;

import com.yaya.sdk.MLog;

/**
 * Created by ober on 2016/10/29.
 */
public class PrefDNSCache implements DNSCache {

    private static final String TAG = "PrefDNSCache";

    private static final String PREF_NAME = "YayaHosts";

    private SharedPreferences mPref;

    public static DNSCache from(Context context) {
        return new PrefDNSCache(context);
    }

    private PrefDNSCache(Context c) {
        mPref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public String getIpByHost(String hostName) {
        MLog.d(TAG, "getIpByHost(" + hostName + ")");
        if(hostName == null) {
            throw new NullPointerException("null host");
        }
        String ip = mPref.getString(hostName, null);
        MLog.d(TAG, "result:" + ip);
        return ip;
    }

    @Override
    public void saveHostAndIp(String host, String ip) {
        MLog.d(TAG, "saveHostAndIp(" + host + "," + ip + ")");
        if(host == null) {
            throw new NullPointerException("null host");
        }
        if(ip == null) {
            throw new NullPointerException("null ip");
        }
        mPref.edit().putString(host, ip).apply();
    }

    @Override
    public void removeHost(String host) {
        MLog.d(TAG, "removeHost(" + host + ")");
        if(mPref.contains(host)) {
            mPref.edit().remove(host).apply();
        }
    }

    @Override
    public void clearAll() {
        MLog.d(TAG, "clearAll");
        mPref.edit().clear().apply();
    }
}
