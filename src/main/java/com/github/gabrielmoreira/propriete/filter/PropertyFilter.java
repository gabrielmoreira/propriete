package com.github.gabrielmoreira.propriete.filter;

public interface PropertyFilter {

	public static final PropertyFilter TRUE = new BooleanPropertyFilter(true);
	public static final PropertyFilter FALSE = new BooleanPropertyFilter(false);

	public boolean filter(String key);

}
