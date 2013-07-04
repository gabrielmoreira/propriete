package com.github.gabrielmoreira.propriete.source;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class SmartPropertyTransformerTest {

	@Test
	public void shouldCreateSmartProperties() {
		// given
		Properties properties = new Properties();
		properties.put("FULL_Name", "Gabriel");
		// when
		Properties newProperties = new SmartPropertyTransformer().transform(properties);
		// then
		assertEquals("Gabriel", newProperties.get("FULL_Name"));
		assertEquals("Gabriel", newProperties.get("FULL_NAME"));
		assertEquals("Gabriel", newProperties.get("full.name"));
		assertEquals(3, newProperties.size());
	}
}
