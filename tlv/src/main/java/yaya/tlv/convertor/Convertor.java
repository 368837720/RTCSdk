package yaya.tlv.convertor;

import yaya.tlv.convertor.unsigned.Unsigned;

public interface Convertor<SRC, TARGET> {
    public TARGET encode(SRC src, Unsigned unsigned);

    public SRC decode(TARGET target);
}
