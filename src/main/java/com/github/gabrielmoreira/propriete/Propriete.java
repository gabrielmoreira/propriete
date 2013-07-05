package com.github.gabrielmoreira.propriete;

import java.lang.reflect.Proxy;
import java.util.Properties;

import com.github.gabrielmoreira.propriete.converter.SimpleConverter;
import com.github.gabrielmoreira.propriete.placeholder.StringSubstitutionPlaceholderResolver;
import com.github.gabrielmoreira.propriete.source.ConfigSource;
import com.github.gabrielmoreira.propriete.source.PropertiesConfigSource;

public class Propriete {

	static final String DEFAULT_NULL_STRING_VALUE = "!$!#null#!$!";

	private ConfigContext configContext;

	public Propriete(ConfigContext configContext) {
		this.configContext = configContext;
	}

	public Propriete(ConfigSource configSource) {
		this(new ConfigContext(configSource, new SimpleConverter(), new StringSubstitutionPlaceholderResolver()));
	}

	public Propriete(Properties properties) {
		this(new PropertiesConfigSource(properties));
	}

	public static <T> T getInstance(Class<T> configType, ConfigContext configContext) {
		return new Propriete(configContext).getInstance(configType);
	}

	public static <T> T getInstance(Class<T> configType, ConfigSource configSource) {
		return new Propriete(configSource).getInstance(configType);
	}

	public static <T> T getInstance(Class<T> configType, Properties properties) {
		return new Propriete(properties).getInstance(configType);
	}

	public <T> T getInstance(Class<T> configType) {
		return getInstance(new DynamicConfig(configContext, configType));
	}

	@SuppressWarnings("unchecked")
	public <T> T getInstance(DynamicConfig dynamicConfig) {
		return (T) Proxy.newProxyInstance( //
				Thread.currentThread().getContextClassLoader(), // 
				new Class[] { dynamicConfig.getType() }, //
				dynamicConfig);
	}

	public ConfigContext getConfigContext() {
		return configContext;
	}

}
