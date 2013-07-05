package com.github.gabrielmoreira.propriete;

import java.util.Map;

import com.github.gabrielmoreira.propriete.converter.Converter;
import com.github.gabrielmoreira.propriete.placeholder.ConfigPlaceholderResolver;
import com.github.gabrielmoreira.propriete.source.ConfigSource;
import com.github.gabrielmoreira.propriete.visitor.PropertyVisitor;

public class ConfigContext {

	private ConfigPlaceholderResolver placeholderResolver;
	private ConfigSource configSource;
	private Converter converter;

	public ConfigContext(ConfigSource configSource, Converter converter) {
		this.configSource = configSource;
		this.converter = converter;
	}

	public ConfigContext(ConfigSource configSource, Converter converter, ConfigPlaceholderResolver configPlaceholderResolver) {
		this(configSource, converter);
		this.placeholderResolver = configPlaceholderResolver;
	}

	public Object get(String propertyKey) {
		return configSource.get(propertyKey);
	}

	public Object convert(Object object, Class<?> propertyType) {
		return converter.convert(object, propertyType);
	}

	public Map<String, Object> filterStartWith(String prefix) {
		return configSource.startsWith(prefix);
	}

	public Object resolvePlaceholders(Object value) {
		if (placeholderResolver != null)
			return placeholderResolver.resolvePlaceholders(this, value);
		return value;
	}

	public void visit(PropertyVisitor propertyVisitor) {
		configSource.visit(propertyVisitor);
	}

}
