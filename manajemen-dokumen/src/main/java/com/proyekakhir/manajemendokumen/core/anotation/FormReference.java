package com.proyekakhir.manajemendokumen.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormReference {
    String label();
    boolean required() default false;
    String type() default "select";
    String url() default "";
    String jsonValues() default "";
    String key() default "";
    String value() default "";
}
