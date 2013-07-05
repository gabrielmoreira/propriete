package com.github.gabrielmoreira.propriete.source;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.gabrielmoreira.propriete.transformer.SmartMapTransformer;

public class SmartPropertyTransformerTest {

	@Test
	public void shouldCreateSmartProperties() {
		// given
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("FULL_Name", "Gabriel");
		// when
		Map<String, Object> newMap = new SmartMapTransformer().transform(map);
		// then
		assertEquals("Gabriel", newMap.get("FULL_Name"));
		assertEquals("Gabriel", newMap.get("FULL_NAME"));
		assertEquals("Gabriel", newMap.get("full.name"));
		assertEquals(3, newMap.size());
	}
}
