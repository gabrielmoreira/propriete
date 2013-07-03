package com.github.gabrielmoreira.propriete.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfigSource implements ConfigSource {

	private Properties properties;

	public PropertiesConfigSource(Properties properties) {
		this.properties = properties;
	}

	public Object get(String key) {
		return properties.getProperty(key);
	}

	public Set<Entry<String, Object>> filterStartWith(String prefix) {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		Map<String, Object> section = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : entrySet) {
			String key = (String) entry.getKey();
			if (key.startsWith(prefix))
				section.put(key, entry.getValue());
		}
		return section.entrySet();
	}

}
