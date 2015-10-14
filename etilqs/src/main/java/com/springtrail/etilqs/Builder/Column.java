package com.springtrail.etilqs.Builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kirk on 9/23/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    //sqlite data types
    enum columnTypeEnum {
        TEXT,
        NUMERIC,
        INTEGER,
        REAL,
        BLOB
    }

    //java data types
    enum dataTypeEnum {
        INTEGER,
        STRING,
        LONG,
        SHORT,
        DOUBLE,
        FLOAT,
        BOOLEAN,
        BYTE
    }

    String name();

    columnTypeEnum type() default columnTypeEnum.TEXT;

    boolean nullable() default true;

    String defaultValue() default "";

    String check() default "";

    String expression() default "";


}
