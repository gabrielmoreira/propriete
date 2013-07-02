package com.github.gabrielmoreira.propriete.source;

import java.util.Properties;

public class PropertiesConfigSource implements ConfigSource {

	private Properties properties;

	public PropertiesConfigSource(Properties properties) {
		this.properties = properties;
	}

	public Object get(String key) {
		return properties.getProperty(key);
	}

}
