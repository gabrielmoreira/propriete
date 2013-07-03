package com.github.gabrielmoreira.propriete.source;

import java.util.Map.Entry;
import java.util.Set;

public interface ConfigSource {

	Object get(String key);

	Set<Entry<String, Object>> filterStartWith(String prefix);

}
