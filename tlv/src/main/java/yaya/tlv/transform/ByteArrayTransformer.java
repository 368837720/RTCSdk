package yaya.tlv.transform;

import yaya.tlv.TlvStore;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.util.Bits;

/**
 * Created by wy on 13-8-13.
 */
public class ByteArrayTransformer implements Transformer<byte[], byte[]> {
    @Override
    public byte[] encode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) throws Exception {
        if (null == bytes || bytes.length == 0) {
            return null;
        } else {
            if (null != bytes) {
                return ArrayUtils.addAll(ArrayUtils.addAll(
                        new byte[]{(byte) tlvFieldMeta.getTag()},
                        Bits.putShort((short) bytes.length)), bytes);
            } else {
                return null;
            }
        }
    }

    @Override
    public byte[] decode(byte[] bytes, TlvFieldMeta tlvFieldMeta,
                         TlvStore tlvStore) throws Exception {
        return bytes;
    }
}
