package com.github.gabrielmoreira.propriete.placeholder;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.gabrielmoreira.propriete.ConfigContext;
import com.github.gabrielmoreira.propriete.ProprieteException;

public class StringSubstitutionPlaceholderResolver implements ConfigPlaceholderResolver {

	private Pattern placeholderPattern = Pattern.compile("\\$\\{(.*?)(?::(.*?))?\\}");

	public Object resolvePlaceholders(ConfigContext context, Object value) {
		if (value == null)
			return null;
		if (value.getClass() == String.class) {
			return findAndReplacePlaceholders(context, (String) value, new HashSet<String>(), new HashSet<String>());
		}
		return value;
	}

	private String findAndReplacePlaceholders(ConfigContext context, String value, Set<String> unresolvedKeys, Set<String> resolvingKeys) {
		Matcher matcher = placeholderPattern.matcher(value);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String key = matcher.group(1);
			String defaultValue = matcher.group(2);
			Object propertyValue = context.get(key, false);
			if (propertyValue == null)
				propertyValue = defaultValue;
			String replaceText = (String) context.convert(propertyValue, String.class);
			if (replaceText == null) {
				unresolvedKeys.add(key);
			} else {
				if (!resolvingKeys.contains(key)) {
					resolvingKeys.add(key);
					String resolvedPlaceholder = findAndReplacePlaceholders(context, replaceText, unresolvedKeys, resolvingKeys);
					resolvingKeys.remove(key);
					matcher.appendReplacement(sb, Matcher.quoteReplacement(resolvedPlaceholder));
				} else {
					throw new ProprieteException("Circular resolving property '" + key + "'");
				}
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
