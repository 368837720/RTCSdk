package com.yaya.sdk.http.dns;

import android.content.Context;

import com.yaya.sdk.MLog;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by ober on 2016/11/24.
 */
public class DnsLoader {

    private static final String TAG = "DnsLoader";

    private static final long INIT_DNS_TIMEOUT;
    private static final Map<String, String> systemDefMap;

    static {
        INIT_DNS_TIMEOUT = 2500;
        systemDefMap = new HashMap<>(2);
        systemDefMap.put("u01.aya.yunva.com ", "107.150.106.155");
        //systemDefMap.put("c.yunva.com", "182.92.23.175");
        systemDefMap.put("mixvoice.yunva.com", "60.205.128.254");
    }

    final DNSCache mDNSCache;
    final Map<String, String> mCustomDnsMap;
    final long mResolveTimeout;

    public DnsLoader(Context c, Map<String, String> defDnsMap, Long resolveDnsTimeout) {
        mDNSCache = PrefDNSCache.from(c);
        mCustomDnsMap = defDnsMap;
        if (resolveDnsTimeout != null) {
            mResolveTimeout = resolveDnsTimeout;
        } else {
            mResolveTimeout = INIT_DNS_TIMEOUT;
        }
    }

    public void loadDnsBackground(String serverUrl) {
        String host;
        try {
            URL url = new URL(serverUrl);
            host = url.getHost();
        } catch (MalformedURLException e) {
            MLog.w(TAG, "MalformedURLException: " + e.getMessage());
            return;
        }

        if (mResolveTimeout < 0) {
            useDefaultDnsMap(host);
            return;
        }
        ResolveDnsTask resolveDnsTask = new ResolveDnsTask(host);
        Thread t = new Thread(resolveDnsTask, "DnsLoaderThread");
        t.setDaemon(true);
        t.start();
    }

    private static String syncResolveDns(String host) {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            MLog.w(TAG, "resolveDns " + e.getClass().getSimpleName()
                    + ":" + e.getMessage());
            return null;
        }
    }

    private static FutureTask<String> asyncResolveDns(final String host) {
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return syncResolveDns(host);
            }
        });
        Thread t = new Thread(task, "DnsResolveThread");
        t.setDaemon(true);
        t.start();
        return task;
    }

    private void useDefaultDnsMap(String host) {

        if (mCustomDnsMap != null && mCustomDnsMap.containsKey(host)) {
            String ip = mCustomDnsMap.get(host);
            mDNSCache.saveHostAndIp(host, ip);
            MLog.i(TAG, "use custom default dns <" + host + "," + ip + ">");
        } else if (systemDefMap.containsKey(host)) {
            String ip = systemDefMap.get(host);
            mDNSCache.saveHostAndIp(host, ip);
            MLog.i(TAG, "use system default dns <" + host + "," + ip + ">");
        }

    }

    private class ResolveDnsTask implements Runnable {

        String host;

        ResolveDnsTask(String host) {
            this.host = host;
        }

        @Override
        public void run() {
            FutureTask<String> task = asyncResolveDns(host);
            try {
                String ip = task.get(mResolveTimeout, TimeUnit.MILLISECONDS);
                if (ip != null) {
                    MLog.i(TAG, "dns resolved:<" + host + "," + ip + ">");
                    mDNSCache.saveHostAndIp(host, ip);
                } else {
                    MLog.w(TAG, "unknown host:" + host);
                    useDefaultDnsMap(host);
                }
            } catch (InterruptedException e) {
                MLog.w(TAG, "ResolveDnsTask interrupt " + e.getMessage());
                useDefaultDnsMap(host);
            } catch (ExecutionException e) {
                MLog.w(TAG, "ResolveDnsTask exe exception " + e.getMessage());
                useDefaultDnsMap(host);
            } catch (TimeoutException e) {
                MLog.w(TAG, "ResolveDnsTask timeout " + e.getMessage());
                useDefaultDnsMap(host);
            }
        }
    }
}
