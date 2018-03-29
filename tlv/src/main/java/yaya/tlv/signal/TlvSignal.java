package yaya.tlv.signal;

import yaya.tlv.Tlvable;
import yaya.tlv.header.TlvAccessHeader;

public abstract class TlvSignal implements Tlvable {
    protected TlvAccessHeader header;

    public TlvAccessHeader getHeader() {
        return header;
    }

    public void setHeader(TlvAccessHeader header) {
        this.header = header;
    }
}
