package com.github.gabrielmoreira.propriete.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.gabrielmoreira.propriete.filter.PropertyFilter;

public class FilterPropertyVisitor implements PropertyVisitor {

	private Map<String, Object> filtered = new HashMap<String, Object>();
	private PropertyFilter propertyFilter;

	public FilterPropertyVisitor(PropertyFilter propertyFilter) {
		this.propertyFilter = propertyFilter;
	}

	public void visit(String key, Object value) {
		if (propertyFilter.filter(key)) {
			filtered.put(key, value);
		}
	}

	public Map<String, Object> getFiltered() {
		return filtered;
	}

}
