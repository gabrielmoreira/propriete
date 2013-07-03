package com.github.gabrielmoreira.propriete.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.gabrielmoreira.propriete.ProprieteException;

public class SimpleConverter implements Converter {

	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = new HashMap<Class<?>, Class<?>>();
	private static final Map<Class<?>, Class<?>> INVERSE_PRIMITIVE_WRAPPERS = new HashMap<Class<?>, Class<?>>();

	static {
		PRIMITIVE_WRAPPERS.put(boolean.class, Boolean.class);
		PRIMITIVE_WRAPPERS.put(byte.class, Byte.class);
		PRIMITIVE_WRAPPERS.put(char.class, Character.class);
		PRIMITIVE_WRAPPERS.put(double.class, Double.class);
		PRIMITIVE_WRAPPERS.put(float.class, Float.class);
		PRIMITIVE_WRAPPERS.put(int.class, Integer.class);
		PRIMITIVE_WRAPPERS.put(long.class, Long.class);
		PRIMITIVE_WRAPPERS.put(short.class, Short.class);

		INVERSE_PRIMITIVE_WRAPPERS.put(Boolean.class, boolean.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Byte.class, byte.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Character.class, char.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Double.class, double.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Float.class, float.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Integer.class, int.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Long.class, long.class);
		INVERSE_PRIMITIVE_WRAPPERS.put(Short.class, short.class);
	}
	private Map<Key, Converter> converters = new HashMap<SimpleConverter.Key, Converter>();

	public Object convert(Object object, Class<?> toType) {
		if (object == null)
			return null;
		if (toType.isAssignableFrom(object.getClass()))
			return object;
		Converter converter = getConverterForKey(new Key(wrap(object.getClass()), wrap(toType)));
		if (converter != null)
			return converter.convert(object, toType);
		throw new ProprieteException("Unsupported conversion from " + object.getClass() + " to " + toType);
	}

	private Converter getConverterForKey(Key key) {
		Converter converter = converters.get(key);
		if (converter == null)
			converter = tryCreateConverterForKey(key);
		return converter;
	}

	private Converter tryCreateConverterForKey(Key key) {
		if (key.to == String.class)
			return new ToStringConverter();
		if (key.to == Boolean.class)
			return new ToBooleanConverter();
		Converter converter = tryMethodConverter(key);
		if (converter == null)
			converter = tryConstructorConverter(key);
		return converter;
	}

	private Converter tryMethodConverter(Key key) {
		Method method = findBestMethod(key.from, key.to);
		if (method != null)
			return new ReflectionMethodConverter(method);
		return null;
	}

	private Converter tryConstructorConverter(Key key) {
		Constructor<?> constructor = findConstructor(key.from, key.to);
		if (constructor != null)
			return new ReflectionConstructorConverter(constructor);
		return null;
	}

	private Method findBestMethod(Class<?> from, Class<?> to) {
		try {
			Method method = to.getMethod("valueOf", from);
			if (Modifier.isStatic(method.getModifiers()))
				return method;
			method = to.getMethod("valueOf", unwrap(from));
			if (Modifier.isStatic(method.getModifiers()))
				return method;
		} catch (Exception e) {
		}
		return null;
	}

	private Constructor<?> findConstructor(Class<?> from, Class<?> to) {
		try {
			return to.getConstructor(from);
		} catch (Exception e) {
		}
		try {
			return to.getConstructor(unwrap(from));
		} catch (Exception e) {
		}
		return null;
	}

	private Class<?> wrap(Class<?> to) {
		return PRIMITIVE_WRAPPERS.containsKey(to) ? PRIMITIVE_WRAPPERS.get(to) : to;
	}

	private Class<?> unwrap(Class<?> to) {
		return INVERSE_PRIMITIVE_WRAPPERS.containsKey(to) ? INVERSE_PRIMITIVE_WRAPPERS.get(to) : to;
	}

	private static class ReflectionMethodConverter implements Converter {
		private Method method;

		public ReflectionMethodConverter(Method method) {
			this.method = method;
		}

		public Object convert(Object object, Class<?> toType) {
			try {
				return method.invoke(null, object);
			} catch (Exception e) {
				throw new ProprieteException(e);
			}
		}
	}

	private static class ReflectionConstructorConverter implements Converter {
		private Constructor<?> constructor;

		public ReflectionConstructorConverter(Constructor<?> constructor) {
			this.constructor = constructor;
		}

		public Object convert(Object object, Class<?> toType) {
			try {
				return constructor.newInstance(object);
			} catch (Exception e) {
				throw new ProprieteException(e);
			}
		}
	}

	private static class ToStringConverter implements Converter {
		public Object convert(Object object, Class<?> toType) {
			return object.toString();
		}
	}

	private static class ToBooleanConverter implements Converter {
		private static final Set<String> NO_VALUES = new HashSet<String>();
		static {
			NO_VALUES.add("no");
			NO_VALUES.add("n");
			NO_VALUES.add("0");
			NO_VALUES.add("false");
		}

		public Object convert(Object object, Class<?> toType) {
			String value = object.toString().trim().toLowerCase();
			if (value.isEmpty())
				return null;
			return !NO_VALUES.contains(value);
		}
	}

	private static class Key {
		private Class<?> from;
		private Class<?> to;

		public Key(Class<?> from, Class<?> to) {
			super();
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

	}

}
