package com.github.gabrielmoreira.propriete;

import java.util.Map.Entry;
import java.util.Set;

import com.github.gabrielmoreira.propriete.converter.Converter;
import com.github.gabrielmoreira.propriete.source.ConfigSource;

public class ConfigContext {

	private ConfigSource configSource;
	private Converter converter;

	public ConfigContext(ConfigSource configSource, Converter converter) {
		this.configSource = configSource;
		this.converter = converter;
	}

	public Object get(String propertyKey) {
		return configSource.get(propertyKey);
	}

	public Object convert(Object object, Class<?> propertyType) {
		return converter.convert(object, propertyType);
	}

	public Set<Entry<String, Object>> filterStartWith(String prefix) {
		return configSource.filterStartWith(prefix);
	}

}
