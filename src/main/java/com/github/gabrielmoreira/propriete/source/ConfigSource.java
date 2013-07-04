package com.github.gabrielmoreira.propriete.source;

import java.util.Map;

public interface ConfigSource {

	Object get(String key);

	Map<String, Object> filterStartWith(String prefix);

}
