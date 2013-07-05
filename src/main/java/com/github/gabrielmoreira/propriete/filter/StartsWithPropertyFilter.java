package com.github.gabrielmoreira.propriete.filter;

public class StartsWithPropertyFilter implements PropertyFilter {

	private String prefix;

	public StartsWithPropertyFilter(String prefix) {
		this.prefix = prefix;
	}

	public boolean filter(String key) {
		return key != null && key.startsWith(prefix);
	}

}
