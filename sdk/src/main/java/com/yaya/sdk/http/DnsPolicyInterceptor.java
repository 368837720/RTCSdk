package com.yaya.sdk.http;

import android.text.TextUtils;

import com.yaya.sdk.MLog;
import com.yaya.sdk.http.dns.DNSCache;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 由于某些dns服务器非常慢，通过缓存ip优化请求效率
 * Created by ober on 2016/10/28.
 */
class DnsPolicyInterceptor implements Interceptor {

    private static final String TAG = "DnsPolicyInterceptor";

    private DNSCache mDnsCache;

    public static Interceptor create(DNSCache cache) {
        return new DnsPolicyInterceptor(cache);
    }

    private DnsPolicyInterceptor(DNSCache dnsCache) {
        this.mDnsCache = dnsCache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        String host = url.host();
        MLog.d(TAG, "intercept request url=" + url.toString());
        if(checkIsIpv4(host)) {
            //如果是ip
            //直接请求
            MLog.d(TAG, "Is Ipv4, not intercept");
            return chain.proceed(request);
        } else if(checkIsIpv6(host)) {
            MLog.d(TAG, "Is Ipv6, not intercept");
            return chain.proceed(request);
        }

        String savedIp = mDnsCache.getIpByHost(host);

        if(savedIp == null) {
            MLog.d(TAG, "no cached ip for:" + host);
            //没有缓存的ip
            String ip = getIpByHost(host);
            MLog.d(TAG, "query ip returned:" + ip);
            String newUrl = replaceIp(url.toString(), host, ip);
            MLog.d(TAG, "proceed with new url:" + newUrl);
            Request newRequest = request.newBuilder()
                    .url(newUrl).build();
            Response response = chain.proceed(newRequest);
            if(response.isSuccessful()) {
                mDnsCache.saveHostAndIp(host, ip);
            }
            return response;
        }

        MLog.d(TAG, "cached ip found:" + savedIp);
        //有缓存的ip
        String newUrl = replaceIp(url.toString(), host, savedIp);
        Request newRequest = request.newBuilder()
                        .url(newUrl).build();
        MLog.d(TAG, "proceed with new url:" + newUrl);
        Response response = chain.proceed(newRequest);
        if(response.isSuccessful()) {
            //成功了
            return response;
        }
        MLog.w(TAG, "response err:" + response.message());
        //不成功则清除dns缓存。重新用域名请求。
        mDnsCache.removeHost(host);
        MLog.d(TAG, "response fail, remove this cached ip");
        MLog.d(TAG, "process url:" + url);
        newRequest = request.newBuilder()
                .url(url).build();
        response = chain.proceed(newRequest);
        return response;
    }

    private static String replaceIp(String url, String host, String ip) {
        if(checkIsIpv6(ip)) {
            return url.replace(host, "[" + ip + "]");
        }
        return url.replace(host, ip);
    }

    private static String getIpByHost(String host) throws UnknownHostException {
        MLog.d(TAG, "getIpByHost(" + host + ")");
        InetAddress inetAddress = InetAddress.getByName(host);
        String hostAddress = inetAddress.getHostAddress();
        MLog.d(TAG, "hostAddress=" + hostAddress);
        return hostAddress;
    }

    //检查格式，并不一定就是ipv4
    private static boolean checkIsIpv4(String ip) {
        String[] ss = ip.split("\\.");
        if(ss.length != 4) {
            return false;
        }
        for(String s : ss) {
            if(!TextUtils.isDigitsOnly(s)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkIsIpv6(String ip) {
        String[] ss = ip.split(":");
        return ss.length > 4;
    }
}
