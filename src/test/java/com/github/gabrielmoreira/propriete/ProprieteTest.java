package com.github.gabrielmoreira.propriete;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class ProprieteTest {

	@Test
	public void shouldReturnSimpleValues() {
		// given
		Properties properties = new Properties();
		properties.put("value", "123");
		// when
		Simple simple = Propriete.getInstance(Simple.class, properties);
		// then
		assertEquals(123, simple.value());
	}

	@Test
	public void shouldReturnNestedValues() {
		// given
		Properties properties = new Properties();
		properties.put("nested.value", "123");
		// when
		App app = Propriete.getInstance(App.class, properties);
		// then
		assertNotNull(app.nested());
		assertEquals(new BigDecimal("123"), app.nested().value());
	}

	@Test(expected = RequiredPropertyException.class)
	public void shouldThrowsExceptionWhenPropertyNotFound() {
		// given
		Properties properties = new Properties();
		// when
		Propriete.getInstance(Simple.class, properties).value();
	}

	@Test
	public void shouldFilterAndReturnAllPropertiesStartingWithPrefix() {
		// given
		Properties properties = new Properties();
		properties.put("app.database.url", "Alice");
		properties.put("app.database.pass", "Bob");
		properties.put("app.other", "1");
		properties.put("app.other.y", "2");
		// when
		AppWithSection appWithSection = Propriete.getInstance(AppWithSection.class, properties);
		// then
		assertNotNull(appWithSection.getDatabaseConfig());
		assertEquals(new HashSet<String>(Arrays.asList("app.database.url", "app.database.pass")), appWithSection.getDatabaseConfig().keySet());
	}

	@Test
	public void shouldFilterAndChangePrefixForAllPropertiesStartingWithPrefix() {
		// given
		Properties properties = new Properties();
		properties.put("app.jpa.show_sql", "true");
		properties.put("app.jpa.generateDDL", "false");
		properties.put("app.other", "1");
		properties.put("app.other.y", "2");
		// when
		AppWithSection appWithSection = Propriete.getInstance(AppWithSection.class, properties);
		// then
		assertNotNull(appWithSection.getHibernateConfig());
		// [app.jpa].show_sql was renamed to [hibernate].show_sql
		assertEquals(new HashSet<String>(Arrays.asList("hibernate.show_sql", "hibernate.generateDDL")), appWithSection.getHibernateConfig().keySet());
	}

	@Test(expected = RequiredPropertyException.class)
	public void shouldThrowsExceptionWhenRequiredSectionNotFound() {
		Properties properties = new Properties();
		// when
		AppWithSection appWithSection = Propriete.getInstance(AppWithSection.class, properties);
		// then
		assertNotNull(appWithSection.getDatabaseConfig());
	}

	@Test()
	public void shouldNotThrowsExceptionWhenOptionalSectionNotFound() {
		Properties properties = new Properties();
		// when
		AppWithSection appWithSection = Propriete.getInstance(AppWithSection.class, properties);
		// then
		assertNotNull(appWithSection.getHibernateConfig());
		assertTrue(appWithSection.getHibernateConfig().isEmpty());
	}

	public interface Simple {
		public int value();
	}

	public interface App {
		@ConfigProperty(name = "nested")
		public Nested nested();
	}

	@Config
	public interface Nested {
		public BigDecimal value();
	}

	@Config(prefix = "app")
	public interface AppWithSection {
		@ConfigProperty(name = "jpa", newKeyPrefix = "hibernate", optional = true)
		Properties getHibernateConfig();

		@ConfigProperty(name = "database")
		Map<String, Object> getDatabaseConfig();

	}

}
