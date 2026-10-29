package com.proyekakhir.manajemendokumen.auth;

import com.proyekakhir.manajemendokumen.constant.RoleUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Secured {
    RoleUser[] value() default {};
}
