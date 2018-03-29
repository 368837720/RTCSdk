package com.yaya.sdk.http.dns;

/**
 * 域名记录
 * Created by ober on 2016/10/29.
 */
public interface DNSCache {
    /**
     * 通过域名获取IP
     * @param hostName host域名
     * @return 对应的IP 没有则null
     */
    String getIpByHost(String hostName);

    /**
     * 存储域名-IP对应关系
     * @param host 域名
     * @param ip 对应的IP
     */
    void saveHostAndIp(String host, String ip);

    /**
     * 删除域名host的记录
     * @param host
     */
    void removeHost(String host);

    /**
     * 清除所有
     */
    void clearAll();
}
