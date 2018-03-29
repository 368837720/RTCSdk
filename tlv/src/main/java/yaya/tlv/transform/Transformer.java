package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.meta.TlvFieldMeta;

public interface Transformer<SRC, TARGET> {
    public TARGET encode(SRC src, TlvFieldMeta tlvFieldMeta, TlvStore tlvStore)
            throws Exception;

    public SRC decode(TARGET target, TlvFieldMeta tlvFieldMeta,
                      TlvStore tlvStore) throws Exception;
}
