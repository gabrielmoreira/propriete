package com.github.gabrielmoreira.propriete.transformer;

import java.util.Map;

public interface MapTransformer {
	public Map<String, Object> transform(Map<String, Object> source);
}
