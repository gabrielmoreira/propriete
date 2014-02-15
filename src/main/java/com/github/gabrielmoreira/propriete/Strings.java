package com.github.gabrielmoreira.propriete;

class Strings {

	public static String buildPath(String delimiter, String... segments) {
		String path = "";
		if (segments == null)
			return path;
		for (String segment : segments)
			path = concatPathSegment(delimiter, path, segment);
		if (path.equals(delimiter) || path.isEmpty())
			return "";
		return path.substring(1);
	}

	public static String concatPathSegment(String delimiter, String path, String pathSegment) {
		if (pathSegment != null && !pathSegment.isEmpty()) {
			path += delimiter + pathSegment;
		}
		return path;
	}

	public static String normalize(String value) {
		return isNull(value) ? null : value;
	}

	public static boolean isNull(String value) {
		return value == null || Propriete.DEFAULT_NULL_STRING_VALUE.equals(value);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static String toJavaBeanName(String propertyName) {
		if (propertyName.length() > 3 && propertyName.startsWith("get") && Character.isUpperCase(propertyName.charAt(3)))
			propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
		return propertyName;
	}
}
