package com.github.gabrielmoreira.propriete.source;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class CompositeConfigSourceTest {

	@Test
	public void shouldAddMulipleSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("name", "Gabriel");
		MockConfigSource configSourceB = new MockConfigSource().with("name", "Leonardo");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA).addSource(configSourceB);
		// when
		String name = (String) composite.get("name");
		// then
		assertEquals("Leonardo", name);
	}

	@Test
	public void shouldConstructWithMulipleSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("name", "Gabriel");
		MockConfigSource configSourceB = new MockConfigSource().with("name", "Leonardo");
		CompositeConfigSource composite = new CompositeConfigSource(configSourceA, configSourceB);
		// when
		String name = (String) composite.get("name");
		// then
		assertEquals("Leonardo", name);
	}

	@Test
	public void shouldFilterStartWithMulipleSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("my.name", "Gabriel").with("my.a", "a").with("any", "any");
		MockConfigSource configSourceB = new MockConfigSource().with("my.name", "Leonardo").with("my.b", "b").with("other", "other");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA, configSourceB);
		// when
		Map<String, Object> filtered = composite.filterStartWith("my");
		// then
		assertEquals(3, filtered.size());
		assertEquals("Leonardo", filtered.get("my.name"));
		assertEquals("a", filtered.get("my.a"));
		assertEquals("b", filtered.get("my.b"));
	}

	private static class MockConfigSource implements ConfigSource {
		private Map<String, Object> values = new HashMap<String, Object>();

		public MockConfigSource with(String key, Object value) {
			values.put(key, value);
			return this;
		}

		public Object get(String key) {
			return values.get(key);
		}

		public Map<String, Object> filterStartWith(String prefix) {
			Set<Entry<String, Object>> entrySet = values.entrySet();
			Map<String, Object> section = new HashMap<String, Object>();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				if (key.startsWith(prefix))
					section.put(key, entry.getValue());
			}
			return section;
		}
	}

}
