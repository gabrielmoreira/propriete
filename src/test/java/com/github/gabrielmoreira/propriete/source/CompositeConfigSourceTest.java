package com.github.gabrielmoreira.propriete.source;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.gabrielmoreira.propriete.filter.PropertyFilter;
import com.github.gabrielmoreira.propriete.visitor.PropertyVisitor;

public class CompositeConfigSourceTest {
	@Test
	public void shouldReturnNullWhenPropertyDoesNotExist() {
		// given
		CompositeConfigSource composite = new CompositeConfigSource();
		// when
		Object result = composite.get("test");
		// then
		assertNull(result);
	}

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
		CompositeConfigSource composite = CompositeConfigSource.with(configSourceA, configSourceB);
		// when
		String name = (String) composite.get("name");
		// then
		assertEquals("Leonardo", name);
	}

	@Test
	public void shouldFilterAllSourcesForPropertiesStartingWithPrefix() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("my.name", "Gabriel").with("my.a", "a").with("any", "any");
		MockConfigSource configSourceB = new MockConfigSource().with("my.name", "Leonardo").with("my.b", "b").with("other", "other");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA, configSourceB);
		// when
		Map<String, Object> filtered = composite.startsWith("my");
		// then
		assertEquals(3, filtered.size());
		assertEquals("Leonardo", filtered.get("my.name"));
		assertEquals("a", filtered.get("my.a"));
		assertEquals("b", filtered.get("my.b"));
	}

	@Test
	public void shouldGroupAllSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("my.name", "Gabriel").with("my.a", "a").with("any", "any");
		MockConfigSource configSourceB = new MockConfigSource().with("my.name", "Leonardo").with("my.b", "b").with("other", "other");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA, configSourceB);
		// when
		Map<String, Object> all = composite.all();
		// then
		assertEquals(5, all.size());
		assertEquals("Leonardo", all.get("my.name"));
		assertEquals("a", all.get("my.a"));
		assertEquals("b", all.get("my.b"));
	}

	@Test
	public void shouldFilterAllSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("my.name", "Gabriel").with("my.a", "a").with("any", "any");
		MockConfigSource configSourceB = new MockConfigSource().with("my.name", "Leonardo").with("my.b", "b").with("other", "other");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA, configSourceB);
		// when
		final List<String> keys = new ArrayList<String>();
		Map<String, Object> all = composite.filter(new PropertyFilter() {
			public boolean filter(String key) {
				keys.add(key);
				return true;
			}
		});
		// then
		assertEquals(6, keys.size());
		assertEquals(5, all.size());
		assertEquals("Leonardo", all.get("my.name"));
		assertEquals("a", all.get("my.a"));
		assertEquals("b", all.get("my.b"));
	}

	@Test
	public void shouldVisitAllSources() {
		// given
		MockConfigSource configSourceA = new MockConfigSource().with("my.name", "Gabriel").with("my.a", "a").with("any", "any");
		MockConfigSource configSourceB = new MockConfigSource().with("my.name", "Leonardo").with("my.b", "b").with("other", "other");
		CompositeConfigSource composite = new CompositeConfigSource();
		composite.addSource(configSourceA, configSourceB);
		// when
		final HashSet<String> keys = new HashSet<String>();
		final HashSet<Object> values = new HashSet<Object>();
		composite.visit(new PropertyVisitor() {
			public void visit(String key, Object value) {
				keys.add(key);
				values.add(value);
			}
		});
		// then
		assertEquals(5, keys.size());
		assertEquals(6, values.size());
	}

	private static class MockConfigSource extends HashMapPropertiesConfigSource {

		public MockConfigSource with(String key, Object value) {
			getSource().put(key, value);
			return this;
		}

	}

}
