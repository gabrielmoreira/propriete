package com.github.gabrielmoreira.propriete.source;

import java.util.HashMap;

public class HashMapPropertiesConfigSource extends AbstractMapConfigSource {

	public HashMapPropertiesConfigSource() {
		super(new HashMap<String, Object>());
	}

}
