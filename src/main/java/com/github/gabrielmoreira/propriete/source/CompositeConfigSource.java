package com.github.gabrielmoreira.propriete.source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CompositeConfigSource implements ConfigSource {

	private LinkedList<ConfigSource> sources = new LinkedList<ConfigSource>();

	public CompositeConfigSource(ConfigSource... configSource) {
		addSource(configSource);
	}

	public CompositeConfigSource addSource(ConfigSource... configSources) {
		for (ConfigSource configSource : configSources) {
			this.sources.addLast(configSource);
		}
		return this;
	}

	public LinkedList<ConfigSource> getSources() {
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

	public Map<String, Object> filterStartWith(String prefix) {
		Map<String, Object> section = new HashMap<String, Object>();
		for (ConfigSource configSource : getSources())
			section.putAll(configSource.filterStartWith(prefix));
		return section;
	}

}
