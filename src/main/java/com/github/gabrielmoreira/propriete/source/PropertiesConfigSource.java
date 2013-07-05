package com.github.gabrielmoreira.propriete.source;

import java.util.Map;
import java.util.Properties;

import com.github.gabrielmoreira.propriete.transformer.MapTransformer;

public class PropertiesConfigSource extends AbstractMapConfigSource {

	public PropertiesConfigSource(Properties properties) {
		this(properties, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PropertiesConfigSource(Properties properties, MapTransformer mapTransformer) {
		super((Map) properties, mapTransformer);
	}

	@SuppressWarnings("rawtypes")
	protected Properties getProperties() {
		return (Properties) (Map) getSource();
	}

}
