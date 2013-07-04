package com.github.gabrielmoreira.propriete.source;

import java.util.Map;
import java.util.Properties;

public class SmartPropertyTransformer implements PropertyTransformer {

	public Properties transform(Properties properties) {
		Properties newProperties = new Properties();
		newProperties.putAll(properties);
		transformKey(properties, newProperties);
		return newProperties;
	}

	protected void transformKey(Properties properties, Properties newProperties) {
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			transformProperty(newProperties, (String) entry.getKey(), entry.getValue());
		}
	}

	private void transformProperty(Properties newProperties, String key, Object value) {
		newProperties.put(toSmartKey(key), value);
		newProperties.put(key.toUpperCase(), value);
	}

	protected String toSmartKey(String key) {
		return key.toLowerCase().replace('_', '.');
	}
}