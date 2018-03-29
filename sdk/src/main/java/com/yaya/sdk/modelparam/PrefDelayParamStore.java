package com.yaya.sdk.modelparam;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ober on 2016/12/1.
 */
class PrefDelayParamStore implements DelayParamStore {
    private static final String PREF_NAME = "YayaParam";

    private static final String KEY = "AEC_D";

    private SharedPreferences mPref;

    static DelayParamStore create(Context c) {
        return new PrefDelayParamStore(c);
    }

    PrefDelayParamStore(Context c) {
        mPref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void putDelayParam(int param) {
        mPref.edit().putInt(KEY, param).apply();
    }

    @Override
    public int getDelayParam() {
        return mPref.getInt(KEY, -1);
    }
}
