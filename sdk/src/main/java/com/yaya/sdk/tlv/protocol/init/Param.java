package com.yaya.sdk.tlv.protocol.init;

import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/12/2.
 */
public class Param extends TlvSignal {
    @TlvSignalField(tag = 1)
    private String key;
    @TlvSignalField(tag = 2)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + key + "|" + value + ">";
    }
}
