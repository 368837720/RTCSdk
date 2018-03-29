package com.yaya.sdk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * 仅标识不为空
 * Created by ober on 2016/10/26.
 */
@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface NonNull {
}
