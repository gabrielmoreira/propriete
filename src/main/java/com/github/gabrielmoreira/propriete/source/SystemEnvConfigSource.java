package com.github.gabrielmoreira.propriete.source;

import java.util.Map;
import java.util.Properties;

public class SystemEnvConfigSource extends PropertiesConfigSource implements ConfigSource {

	public SystemEnvConfigSource() {
		this(new SmartPropertyTransformer());
	}

	public SystemEnvConfigSource(PropertyTransformer propertyTransformer) {
		super(getEnvAsProperties(), propertyTransformer);
	}

	private static Properties getEnvAsProperties() {
		Properties properties = new Properties();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();
			properties.setProperty(key, entry.getValue());
		}
		return properties;
	}

}
