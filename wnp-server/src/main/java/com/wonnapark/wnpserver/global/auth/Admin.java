package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.user.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Admin {

    Role role() default Role.ADMIN;
}
