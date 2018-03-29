package yaya.tlv.meta;

import java.lang.reflect.Field;

import yaya.tlv.convertor.Convertor;
import yaya.tlv.convertor.unsigned.Unsigned;

public class TlvHeaderFieldMeta implements Comparable<TlvHeaderFieldMeta> {
    private Field field;
    private int index;
    private Unsigned unsigned;
    private Convertor convertor;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Convertor getConvertor() {
        return convertor;
    }

    public void setConvertor(Convertor convertor) {
        this.convertor = convertor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TlvHeaderFieldMeta{");
        sb.append("unsigned=").append(unsigned);
        sb.append(", field=").append(field);
        sb.append(", index=").append(index);
        sb.append(", transform=").append(convertor);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(TlvHeaderFieldMeta o) {
        if (this.index == o.index) {
            return 0;
        } else if (this.index > o.index) {
            return 1;
        } else {
            return -1;
        }
    }
}
