package com.github.gabrielmoreira.propriete;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {

	String key() default "!$!#null#!$!";

	String name() default "!$!#null#!$!";

	String defaultValue() default "!$!#null#!$!";

	boolean required() default false;

}
