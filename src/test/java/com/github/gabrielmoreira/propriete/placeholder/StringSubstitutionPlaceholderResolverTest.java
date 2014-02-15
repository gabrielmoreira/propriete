package com.github.gabrielmoreira.propriete.placeholder;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import com.github.gabrielmoreira.propriete.ConfigContext;
import com.github.gabrielmoreira.propriete.ProprieteException;
import com.github.gabrielmoreira.propriete.converter.SimpleConverter;
import com.github.gabrielmoreira.propriete.source.PropertiesConfigSource;

public class StringSubstitutionPlaceholderResolverTest {

	private Properties properties = new Properties();
	private ConfigContext configContext = new ConfigContext(new PropertiesConfigSource(properties), new SimpleConverter(), new StringSubstitutionPlaceholderResolver());

	@Test
	public void shouldReplaceOnePlaceholder() {
		properties.put("name", "Gabriel");
		assertEquals("Hello Gabriel!", configContext.resolvePlaceholders("Hello ${name}!"));
	}

	@Test
	public void shouldUseDefaultPlaceholder() {
		assertEquals("Hello Gabriel da silva!", configContext.resolvePlaceholders("Hello ${name:Gabriel da silva}!"));
	}

	@Test
	public void shouldNotUseDefaultPlaceholder() {
		properties.put("name", "Gabriel");
		assertEquals("Hello Gabriel!", configContext.resolvePlaceholders("Hello ${name:Fulano}!"));
	}

	@Test
	public void shouldReplaceMorePlaceholders() {
		properties.put("hi", "Hello");
		properties.put("name", "Gabriel");
		assertEquals("Hello Gabriel!", configContext.resolvePlaceholders("${hi} ${name}!"));
	}

	@Test
	public void shouldNotReplaceUnresolvedPlaceholders() {
		properties.put("hi", "Hello");
		properties.put("name", "Gabriel");
		assertEquals("Hello Gabriel ${inexistent}!", configContext.resolvePlaceholders("${hi} ${name} ${inexistent}!"));
	}

	@Test
	public void shouldDeepResolvePlaceholders() {
		properties.put("firstName", "Gabriel");
		properties.put("name", "${firstName}");
		assertEquals("Hello Gabriel!", configContext.resolvePlaceholders("Hello ${name}!"));
	}

	@Test(expected = ProprieteException.class)
	public void shouldNotLoopResolvingPlaceholders() {
		properties.put("name", "${name}");
		configContext.resolvePlaceholders("Hello ${name}!");
	}

	@Test(expected = ProprieteException.class)
	public void shouldNotCircularLoopResolvingPlaceholders() {
		properties.put("firstName", "${name}");
		properties.put("name", "${firstName}");
		configContext.resolvePlaceholders("Hello ${name}!");
	}

}
