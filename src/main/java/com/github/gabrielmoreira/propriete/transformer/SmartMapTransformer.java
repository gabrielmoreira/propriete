package com.github.gabrielmoreira.propriete.transformer;

import java.util.Map;
import java.util.Properties;

public class SmartMapTransformer implements MapTransformer {

	public Map<String, Object> transform(Map<String, Object> properties) {
		Map<String, Object> newProperties = create();
		newProperties.putAll(properties);
		transformKey(properties, newProperties);
		return newProperties;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<String, Object> create() {
		return (Map) new Properties();
	}

	protected void transformKey(Map<String, Object> properties, Map<String, Object> newProperties) {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			transformProperty(newProperties, entry.getKey(), entry.getValue());
		}
	}

	private void transformProperty(Map<String, Object> newProperties, String key, Object value) {
		newProperties.put(toSmartKey(key), value);
		newProperties.put(key.toUpperCase(), value);
	}

	protected String toSmartKey(String key) {
		return key.toLowerCase().replace('_', '.');
	}
}