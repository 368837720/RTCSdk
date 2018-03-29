package com.yaya.sdk.modelparam;

/**
 * Created by ober on 2016/12/1.
 */
public interface ParamLoader {
    interface LoaderCallback {
        /**
         *
         * @param success 0成功 1失败 2从缓存中获取到
         */
        void onLoadBack(int success, int param);
    }

    void load(LoaderCallback callback);
}
