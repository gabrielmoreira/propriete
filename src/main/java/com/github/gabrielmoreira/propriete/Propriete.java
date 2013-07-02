package com.github.gabrielmoreira.propriete;

import java.lang.reflect.Proxy;

public class Propriete {

	private ConfigContext configContext;

	public Propriete(ConfigContext configContext) {
		this.configContext = configContext;
	}

	public static <T> T getInstance(Class<T> configType, ConfigContext configContext) {
		return new Propriete(configContext).getInstance(configType);
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

}
