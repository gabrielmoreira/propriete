package com.github.gabrielmoreira.propriete;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {

	/**
	 * Absolute property name
	 */
	String key() default Propriete.DEFAULT_NULL_STRING_VALUE;

	/**
	 * Relative property name 
	 */
	String name() default Propriete.DEFAULT_NULL_STRING_VALUE;

	/**
	 * Default property value
	 */
	String defaultValue() default Propriete.DEFAULT_NULL_STRING_VALUE;

	/**
	 * Property is optional? 
	 */
	boolean optional() default false;

	/**
	 * New key property prefix
	 */
	String newKeyPrefix() default Propriete.DEFAULT_NULL_STRING_VALUE;

}
