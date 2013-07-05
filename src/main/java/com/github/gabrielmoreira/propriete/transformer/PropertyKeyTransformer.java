package com.github.gabrielmoreira.propriete.transformer;

public interface PropertyKeyTransformer {

	public static final PropertyKeyTransformer NOOP = new PropertyKeyTransformer() {
		public String transform(String key) {
			return key;
		}
	};

	public String transform(String key);

}
