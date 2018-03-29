package yaya.tlv;

import android.util.SparseArray;

import java.util.SortedSet;

import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.meta.TlvHeaderFieldMeta;

public interface TlvStore {
    public Class getTypeMeta(int moduleId, int msgCode);

    public SparseArray<TlvFieldMeta> getTlvFieldMeta(Class type);

    public SortedSet<TlvHeaderFieldMeta> getTlvHeaderFieldMeta(Class type);
}
