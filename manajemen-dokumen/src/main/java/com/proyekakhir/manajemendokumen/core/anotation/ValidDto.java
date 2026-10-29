package com.proyekakhir.manajemendokumen.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidDto {
    public String[] requiredIn() default "";
    public String[] mustBlankIn() default "";
}
