package com.github.gabrielmoreira.propriete;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicConfig implements InvocationHandler {

	private ConfigContext configContext;
	private Class<?> type;
	private String path;
	private Map<Method, ExecutionHandler> executionHandlers = new HashMap<Method, ExecutionHandler>();

	public DynamicConfig(ConfigContext configContext, Class<?> type) {
		this(configContext, type, null, null);
	}

	public DynamicConfig(ConfigContext configContext, Class<?> type, String parentPath, String prefix) {
		this.configContext = configContext;
		this.type = type;
		this.path = buildPath(type, parentPath, prefix);
	}

	private String buildPath(Class<?> type, String parentPath, String prefix) {
		if (prefix != null)
			return buildPath(parentPath, prefix);
		Config config = type.getAnnotation(Config.class);
		if (config != null)
			return buildPath(parentPath, config.prefix());
		return parentPath;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return getOrCreateExecutionHandler(method).execute(args);
	}

	private ExecutionHandler getOrCreateExecutionHandler(Method method) {
		ExecutionHandler executionHandler = executionHandlers.get(method);
		if (executionHandler == null) {
			executionHandler = createExecutionHandler(method);
			executionHandlers.put(method, executionHandler);
		}
		return executionHandler;
	}

	private ExecutionHandler createExecutionHandler(Method method) {
		ConfigMethodAdapter configMethodAdapter = new ConfigMethodAdapter(method);
		if (configMethodAdapter.isConfigurationType())
			return new GetDynamicConfigExecutionHandler(configMethodAdapter);
		return new GetPropertyExecutionHandler(configMethodAdapter);
	}

	private interface ExecutionHandler {
		public Object execute(Object[] args);
	}

	private class GetDynamicConfigExecutionHandler implements ExecutionHandler {
		private Object instance;

		public GetDynamicConfigExecutionHandler(ConfigMethodAdapter configMethodAdapter) {
			DynamicConfig dynamicConfig = new DynamicConfig(configContext, configMethodAdapter.getType(), getChildPath(configMethodAdapter), getName(configMethodAdapter));
			this.instance = new Propriete(configContext).getInstance(dynamicConfig);
		}

		protected String getName(ConfigMethodAdapter configMethodAdapter) {
			return configMethodAdapter.getDefinedKey() == null ? configMethodAdapter.getDefinedName() : "";
		}

		protected String getChildPath(ConfigMethodAdapter configMethodAdapter) {
			String childPath = configMethodAdapter.getDefinedKey();
			return childPath != null ? childPath : path;
		}

		public Object execute(Object[] args) {
			return instance;
		}
	}

	private class GetPropertyExecutionHandler implements ExecutionHandler {
		private String propertyKey = "";
		private String defaultValue;
		private boolean required;
		private Class<?> propertyType;

		public GetPropertyExecutionHandler(ConfigMethodAdapter configMethodAdapter) {
			this.propertyKey = configMethodAdapter.getKey();
			this.defaultValue = configMethodAdapter.getDefaultValue();
			this.propertyType = configMethodAdapter.getType();
			this.required = configMethodAdapter.isRequired();
		}

		public Object execute(Object[] args) {
			Object object = configContext.get(propertyKey);
			if (object == null)
				object = defaultValue;
			if (object == null && (required || this.propertyType.isPrimitive())) {
				throw new RequiredPropertyException("Property '" + propertyKey + "' not found!");
			}
			return object == null ? null : configContext.convert(object, propertyType);
		}
	}

	private class ConfigMethodAdapter {

		private Method method;

		public ConfigMethodAdapter(Method method) {
			this.method = method;
		}

		public boolean isRequired() {
			return getConfigProperty().required();
		}

		public Class<?> getType() {
			return method.getReturnType();
		}

		public boolean isConfigurationType() {
			return method.getReturnType().isAnnotationPresent(Config.class);
		}

		public ConfigProperty getConfigProperty() {
			ConfigProperty configProperty = method.getAnnotation(ConfigProperty.class);
			return configProperty == null ? Defaults.getDefaultConfigProperty() : configProperty;
		}

		public String getName() {
			String name = getDefinedName();
			return name != null ? name : getJavaBeanName();
		}

		public String getDefinedName() {
			return normalize(getConfigProperty().name());
		}

		public String getKey() {
			String key = getDefinedKey();
			return key != null ? key : buildPath(path, getName());
		}

		public String getDefinedKey() {
			return normalize(getConfigProperty().key());
		}

		public String getDefaultValue() {
			return normalize(getConfigProperty().defaultValue());
		}

		public String getJavaBeanName() {
			return toJavaBeanName(method.getName());
		}

	}

	private static class Defaults {
		private static ConfigProperty CONFIG_PROPERTY;

		@ConfigProperty()
		public static ConfigProperty getDefaultConfigProperty() {
			if (CONFIG_PROPERTY == null) {
				try {
					CONFIG_PROPERTY = Defaults.class.getDeclaredMethod("getDefaultConfigProperty").getAnnotation(ConfigProperty.class);
				} catch (Exception e) {
					throw new ProprieteException(e);
				}
			}
			return CONFIG_PROPERTY;
		}
	}

	protected static String buildPath(String... segments) {
		if (segments == null)
			return "";
		String path = "";
		for (String segment : segments) {
			path = concatPathSegment(path, segment);
		}
		if (path.equals(".") || path.isEmpty())
			return "";
		return path.substring(1);
	}

	protected static String concatPathSegment(String path, String pathSegment) {
		if (pathSegment != null && !pathSegment.isEmpty()) {
			path += "." + pathSegment;
		}
		return path;
	}

	protected static String normalize(String value) {
		return isNull(value) ? null : value;
	}

	protected static boolean isNull(String value) {
		return value == null || "!$!#null#!$!".equals(value);
	}

	protected static String toJavaBeanName(String propertyName) {
		if (propertyName.length() > 3 && propertyName.startsWith("get") && Character.isUpperCase(propertyName.charAt(3)))
			propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
		return propertyName;
	}

	public Class<?> getType() {
		return type;
	}

}
