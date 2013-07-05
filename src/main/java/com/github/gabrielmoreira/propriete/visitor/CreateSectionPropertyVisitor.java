package com.github.gabrielmoreira.propriete.visitor;

import java.util.Map;

import com.github.gabrielmoreira.propriete.filter.StartsWithPropertyFilter;
import com.github.gabrielmoreira.propriete.transformer.PropertyKeyTransformer;

public class CreateSectionPropertyVisitor implements PropertyVisitor {

	private Map<String, Object> section;
	private PropertyKeyTransformer propertyKeyTransformer;
	private StartsWithPropertyFilter propertyFilter;

	public CreateSectionPropertyVisitor(Map<String, Object> section, String propertyKey, PropertyKeyTransformer propertyKeyTransformer) {
		this.section = section;
		this.propertyFilter = new StartsWithPropertyFilter(propertyKey);
		this.propertyKeyTransformer = propertyKeyTransformer;
	}

	public void visit(String key, Object value) {
		if (propertyFilter.filter(key))
			section.put(propertyKeyTransformer.transform(key), value);
	}

	public Map<String, Object> getSection() {
		return section;
	}

}
