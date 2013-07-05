package com.github.gabrielmoreira.propriete.source;

import java.util.Map;

import com.github.gabrielmoreira.propriete.filter.PropertyFilter;
import com.github.gabrielmoreira.propriete.visitor.PropertyVisitor;

public interface ConfigSource {

	Object get(String key);

	Map<String, Object> filter(PropertyFilter propertyFilter);

	Map<String, Object> startsWith(String prefix);

	Map<String, Object> all();

	void visit(PropertyVisitor propertyVisitor);
}
