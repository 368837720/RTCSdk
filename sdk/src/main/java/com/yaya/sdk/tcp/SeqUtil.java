package com.yaya.sdk.tcp;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ober on 2016/11/8.
 */
public class SeqUtil {
    private static AtomicLong atomicLong = new AtomicLong(0);

    public static long newSeq() {
        return atomicLong.incrementAndGet();
    }
}
