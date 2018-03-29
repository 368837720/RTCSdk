package yaya.tlv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import yaya.tlv.convertor.unsigned.Unsigned;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface TlvSignalField {
    int tag();

    Unsigned unsigned() default Unsigned.NONE;
}
