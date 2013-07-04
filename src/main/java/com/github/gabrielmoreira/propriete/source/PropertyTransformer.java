package com.github.gabrielmoreira.propriete.source;

import java.util.Properties;

public interface PropertyTransformer {
	public Properties transform(Properties properties);
}
