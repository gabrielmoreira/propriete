package com.github.gabrielmoreira.propriete.filter;

public class BooleanPropertyFilter implements PropertyFilter {

	private boolean value;

	public BooleanPropertyFilter(boolean value) {
		this.value = value;
	}

	public boolean filter(String key) {
		return value;
	}

}
