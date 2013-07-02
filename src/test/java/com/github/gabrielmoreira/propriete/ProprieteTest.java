package com.github.gabrielmoreira.propriete;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Properties;

import org.junit.Test;

public class ProprieteTest {

	@Test
	public void simple() {
		// given
		Properties properties = new Properties();
		properties.put("value", "123");
		// when
		Simple simple = Propriete.getInstance(Simple.class, properties);
		// then
		assertEquals(123, simple.value());
	}

	@Test
	public void nested() {
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
	public void notFound() {
		// given
		Properties properties = new Properties();
		// when
		Simple simple = Propriete.getInstance(Simple.class, properties);
		// then
		assertEquals(123, simple.value());
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

}
