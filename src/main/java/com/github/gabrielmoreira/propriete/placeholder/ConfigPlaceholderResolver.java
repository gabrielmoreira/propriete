package com.github.gabrielmoreira.propriete.placeholder;

import com.github.gabrielmoreira.propriete.ConfigContext;

public interface ConfigPlaceholderResolver {

	public Object resolvePlaceholders(ConfigContext context, Object value);

}
