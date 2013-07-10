package com.github.gabrielmoreira.propriete.source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.github.gabrielmoreira.propriete.filter.PropertyFilter;
import com.github.gabrielmoreira.propriete.visitor.PropertyVisitor;

public class CompositeConfigSource implements ConfigSource {

	private LinkedList<ConfigSource> sources;

	public CompositeConfigSource() {
		this(new LinkedList<ConfigSource>());
	}

	public CompositeConfigSource(LinkedList<ConfigSource> sources) {
		this.sources = sources;
	}

	public static CompositeConfigSource with(ConfigSource... configSource) {
		return new CompositeConfigSource().addSource(configSource);
	}

	public CompositeConfigSource addSource(ConfigSource... configSources) {
		for (ConfigSource configSource : configSources) {
			this.sources.addLast(configSource);
		}
		return this;
	}

	protected LinkedList<ConfigSource> getSources() {
		return sources;
	}

	public Object get(String key) {
		Iterator<ConfigSource> iterator = getSources().descendingIterator();
		while (iterator.hasNext()) {
			Object value = iterator.next().get(key);
			if (value != null)
				return value;
		}
		return null;
	}

	public Map<String, Object> startsWith(String prefix) {
		Map<String, Object> filtered = new HashMap<String, Object>();
		for (ConfigSource configSource : getSources())
			filtered.putAll(configSource.startsWith(prefix));
		return filtered;
	}

	public Map<String, Object> all() {
		Map<String, Object> filtered = new HashMap<String, Object>();
		for (ConfigSource configSource : getSources())
			filtered.putAll(configSource.all());
		return filtered;
	}

	public Map<String, Object> filter(PropertyFilter propertyFilter) {
		Map<String, Object> filtered = new HashMap<String, Object>();
		for (ConfigSource configSource : getSources())
			filtered.putAll(configSource.filter(propertyFilter));
		return filtered;
	}

	public void visit(PropertyVisitor propertyVisitor) {
		for (ConfigSource configSource : getSources())
			configSource.visit(propertyVisitor);
	}

}
