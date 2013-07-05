package com.github.gabrielmoreira.propriete.transformer;

public class AddPrefixPropertyKeyTransformer implements PropertyKeyTransformer {

	private String prefix;
	private int startOffset;

	public AddPrefixPropertyKeyTransformer(String prefix) {
		this.prefix = prefix;
	}

	public AddPrefixPropertyKeyTransformer(String prefix, int startOffset) {
		this.prefix = prefix;
		this.startOffset = startOffset;
	}

	public String transform(String key) {
		if (key == null)
			return null;
		return prefix + key.substring(startOffset);
	}

}
