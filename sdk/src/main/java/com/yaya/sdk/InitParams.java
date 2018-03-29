package com.yaya.sdk;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Sdk的一些初始化参数<br/>
 * 用于初始化sdk{@link RTV#init(Context, String, VideoTroopsRespondListener, RTV.Env, InitParams, RTV.Mode)}
 * ，所有字段均可设置自定义的值或者为空(为空则取默认的)<br/>
 * 注意：默认值已经是比较合理的参数，请谨慎设置自定义参数<br/>
 * 具体的说明见下面方法说明
 */
public class InitParams {
    private Integer retryCount;
    private Long retryTimeout;

    private Long httpTimeout;
    private Boolean useDNSCache;
    private Map<String, String> customDnsMap = new HashMap<>();
    private Long loadDnsTimeout;

    private boolean isInitMethodAsynchronous = true;

    private Long loginTimeout;
    private Long logoutTimeout;
    private Long micUpTimeout;
    private Long micDownTimeout;
    private Long modeSetTimeout;

    /**
     * 获取一个默认的InitParam,可通过它的getter方法查看具体值，也可以重置他的值
     * @return SDK默认参数
     */
    public static InitParams getDefault() {
        InitParams params = new InitParams();
        params.setHttpTimeout(10000);
        params.setLoadDnsTimeout(2500);
        params.setRetryParams(3, 2000 * 60);
        params.setUseDNSCache(true);
        params.setLoginTimeout(5000);
        params.setMicUpTimeout(3000);
        params.setMicDownTimeout(5000);
        params.setLogoutTimeout(3000);
        params.setModeSetTimeout(3000);
        return params;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Long getRetryTimeout() {
        return retryTimeout;
    }

    /**
     * 设置重连策略<br/>
     * 重连策略:在{retryTimeout}时间段内，最多重连{retryCount}次，最后一次重连失败，则不再重连。<br/>
     * 在连接成功后{retryTimeout}之后，重连次数重置（即又可最多重连{retryCount}次）。<br/>
     * 例如设置(1000, 100000)则可认为是无限重连，设置(0, 0)不重连
     * @param retryCount 在{retryTimeout}时间段内最多重连次数
     * @param retryTimeout 重连时间段
     */
    public void setRetryParams(int retryCount, long retryTimeout) {
        this.retryCount = retryCount;
        this.retryTimeout = retryTimeout;
    }

    /**
     * 设置默认的host-DNS对应关系
     * @param hostName 域名
     * @param ip ip地址
     */
    public void putCustomDns(String hostName, String ip) {
        customDnsMap.put(hostName, ip);
    }

    public boolean isInitMethodAsynchronous() {
        return isInitMethodAsynchronous;
    }

    /**
     * 设置SDK的初始化同步/异步
     * @param asynchronous true异步;false同步
     */
    public void setInitMethodAsynchronous(boolean asynchronous) {
        this.isInitMethodAsynchronous = asynchronous;
    }

    public Map<String, String> getCustomDnsMap() {
        return customDnsMap;
    }

    public Long getLoadDnsTimeout() {
        return loadDnsTimeout;
    }

    /**
     * 设置初始化时DNS解析并缓存的超时时间，设为0则不做DNS解析
     * @param timeout 解析DNS最大时长
     */
    public void setLoadDnsTimeout(long timeout) {
        this.loadDnsTimeout = timeout;
    }

    public Long getHttpTimeout() {
        return httpTimeout;
    }

    /**
     * 设置HTTP请求的超时时间
     * @param httpTimeout http请求超时时间
     */
    public void setHttpTimeout(long httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    public Boolean getUseDNSCache() {
        return useDNSCache;
    }

    /**
     * 设置是否启用DNS本地缓存,默认开启，建议开启
     * @param useDNSCache true启用，false不启用
     */
    public void setUseDNSCache(boolean useDNSCache) {
        this.useDNSCache = useDNSCache;
    }

    public Long getLoginTimeout() {
        return loginTimeout;
    }

    /**
     * 设置登录请求超时时间
     * @param loginTimeout 登录超时时间
     */
    public void setLoginTimeout(long loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    public Long getLogoutTimeout() {
        return logoutTimeout;
    }

    /**
     * 设置登出请求超时时间
     * @param logoutTimeout 登出超时时间
     */
    public void setLogoutTimeout(long logoutTimeout) {
        this.logoutTimeout = logoutTimeout;
    }

    public Long getMicDownTimeout() {
        return micDownTimeout;
    }

    /**
     * 设置下麦请求超时时间
     * @param micDownTimeout 下麦请求超时时间
     */
    public void setMicDownTimeout(long micDownTimeout) {
        this.micDownTimeout = micDownTimeout;
    }

    public Long getMicUpTimeout() {
        return micUpTimeout;
    }

    /**
     * 设置上麦请求超时时间
     * @param micUpTimeout 上麦请求超时时间
     */
    public void setMicUpTimeout(long micUpTimeout) {
        this.micUpTimeout = micUpTimeout;
    }

    public Long getModeSetTimeout() {
        return modeSetTimeout;
    }

    /**
     * 设置语音模式设置请求超时时间
     * @param modeSetTimeout 语音模式设置请求超时时间
     */
    public void setModeSetTimeout(long modeSetTimeout) {
        this.modeSetTimeout = modeSetTimeout;
    }
}
