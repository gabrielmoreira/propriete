package com.github.gabrielmoreira.propriete.builder;

import com.github.gabrielmoreira.propriete.ConfigContext;
import com.github.gabrielmoreira.propriete.source.CompositeConfigSource;
import com.github.gabrielmoreira.propriete.source.ConfigSource;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class ConfigSourceBuilder {

	private StreamLoader streamLoader;

	public ConfigSourceBuilder(StreamLoader streamLoader) {
		this.streamLoader = streamLoader;
	}

	public ConfigSourceBuilder addPropertyFile(String... files) {
		return this;
	}

	public ConfigSource build() {
		return new CompositeConfigSource();
	}

	public ConfigSourceBuilder addSystemProperties() {
		return this;
	}

	public ConfigSourceBuilder addSystemEnv() {
		return this;
	}

	public ConfigSourceBuilder optionalWhenMissingProperty(String propertyKey) {
		return optional();
	}

	private ConfigSourceBuilder optional() {
		return optionalWhen(Predicates.<ConfigContext> alwaysTrue());
	}

	public ConfigSourceBuilder optionalWhen(Predicate<ConfigContext> predicate) {
		return this;
	}

}
