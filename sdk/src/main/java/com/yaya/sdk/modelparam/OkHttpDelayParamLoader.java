package com.yaya.sdk.modelparam;

import com.yaya.sdk.MLog;
import com.yaya.sdk.http.YayaHttp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ober on 2016/12/1.
 */
public abstract class OkHttpDelayParamLoader implements ParamLoader {

    private static final String TAG = "AsyncParamLoader";

    protected static final int DELAY_PARAM_DEF = 100;

    private DelayParamStore mDelayParamStore;

    OkHttpDelayParamLoader(DelayParamStore store) {
        mDelayParamStore = store;
    }

    abstract Request createReq();

    abstract int resolveResp(Response response);

    @Override
    public void load(LoaderCallback callback) {
        MLog.d(TAG, "load");
        Request req = createReq();
        final Call call = YayaHttp.getInstance().getOkHttp().newCall(req);
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Response resp = call.execute();
                if(resp.isSuccessful()) {
                    MLog.d(TAG, "response success");
                    return resolveResp(resp);
                }
                MLog.i(TAG, "response fail " + resp.code() + "," + resp.message());
                return -1;
            }
        });
        Thread t = new Thread(futureTask, "InitLoadPThread");
        t.start();
        int p = -1;
        int success = 0;
        try {
            p = futureTask.get(4000, TimeUnit.MILLISECONDS);
            success = 0;
            if(p > 0) {
                mDelayParamStore.putDelayParam(p);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            MLog.w(TAG, e.getClass().getSimpleName() + ":" + e.getMessage());
            success = 1;
        }
        if(p < 0) {
            p = mDelayParamStore.getDelayParam();
            if(p > 0) {
                success = 2;
            } else {
                p = DELAY_PARAM_DEF;
            }
        }
        callback.onLoadBack(success, p);
    }

}
