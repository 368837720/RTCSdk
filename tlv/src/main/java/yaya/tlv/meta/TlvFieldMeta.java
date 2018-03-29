package yaya.tlv.meta;

import java.lang.reflect.Field;

import yaya.tlv.convertor.unsigned.Unsigned;
import yaya.tlv.transform.Transformer;

public class TlvFieldMeta {
    private Field field;
    private int tag;
    private Unsigned unsigned;
    private Transformer transformer;

    public Unsigned getUnsigned() {
        return unsigned;
    }

    public void setUnsigned(Unsigned unsigned) {
        this.unsigned = unsigned;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TlvFieldMeta{");
        sb.append("unsigned=").append(unsigned);
        sb.append(", field=").append(field);
        sb.append(", tag=").append(tag);
        sb.append(", transform=").append(transformer);
        sb.append('}');
        return sb.toString();
    }
}
