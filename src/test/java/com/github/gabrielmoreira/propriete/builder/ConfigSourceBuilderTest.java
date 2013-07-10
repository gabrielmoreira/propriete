package com.github.gabrielmoreira.propriete.builder;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.github.gabrielmoreira.propriete.ProprieteException;
import com.github.gabrielmoreira.propriete.source.ConfigSource;

public class ConfigSourceBuilderTest {

	@Test
	public void shouldResolveFileLocationPlaceholders() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("a.properties").add("env", "b").add("name", "start");
		loader.file("b.properties").add("name", "end");
		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("a.properties");
		builder.addPropertyFile("${env}.properties");
		ConfigSource source = builder.build();

		// then
		assertEquals("end", (String) source.get("name"));
	}

	@Test
	public void shouldIgnoreOptionalPropertiesWhenMissingSomePropertyKey() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("a.properties").add("name", "end");
		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("a.properties");
		builder.addPropertyFile("${env}.properties").optionalWhenMissingProperty("env");
		ConfigSource source = builder.build();

		// then
		assertEquals("end", (String) source.get("name"));
	}

	@Test(expected = ProprieteException.class)
	public void shouldThrowsErrorWhenMissingProperties() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("a.properties").add("name", "end");
		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("a.properties");
		builder.addPropertyFile("${env}.properties");
		builder.build();
	}

	@Test
	public void supportMultiplePropertyLocationOptions() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("a.properties").add("name", "end");
		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("b.properties", "a.properties");
		ConfigSource source = builder.build();

		// then
		assertEquals("end", (String) source.get("name"));
	}

	@Test(expected = ProprieteException.class)
	public void shouldThrowsErrorWhenCiclicReferences() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("a.properties").add("env", "b");
		loader.file("b.properties").add("env", "a");
		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("a.properties");
		builder.addPropertyFile("${env}.properties");
		builder.build();
	}

	@Test
	public void shouldAddSystemPropertiesProvider() {
		// given
		ConfigSourceBuilder builder = new ConfigSourceBuilder(new PropertiesStreamLoader());
		System.setProperty("shouldAddSystemPropertiesProvider", "true");

		// when
		builder.addSystemProperties();
		ConfigSource source = builder.build();

		// then
		assertEquals("true", (String) source.get("shouldAddSystemPropertiesProvider"));
	}

	@Test
	public void shouldResolveSimpleDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default.properties").add("env", "local");

		loader.file("local.properties").add("security", "memoria");
		loader.file("producao.properties").add("security", "ldap");

		loader.file("memoria.properties").add("security.class", "MemoriaSecurity");
		loader.file("ldap.properties").add("security", "LdapSecurity");

		loader.file("local.memoria.properties").add("users", "usuario-${env}");
		loader.file("local.ldap.properties").add("ldap", "127.0.0.1");
		loader.file("producao.memoria.properties").add("users", "usuario-${env}");
		loader.file("producao.ldap.properties").add("ldap", "ec2-ldap");

		loader.file("external.properties").add("env", "producao");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${env}.properties");
		builder.addPropertyFile("${security}.properties");
		builder.addPropertyFile("${env}.${security}.properties");
		builder.addPropertyFile("external.properties");
		ConfigSource source = builder.build();

		// then
		assertEquals("producao", (String) source.get("env"));
		assertEquals("ldap", (String) source.get("security"));
		assertEquals("LdapSecurity", (String) source.get("security.class"));
		assertEquals("ec2-ldap", (String) source.get("ldap"));
		assertFalse(source.all().keySet().contains("users"));
	}

	@Test
	public void shouldResolveComplexDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default.properties").add("env", "local");

		loader.file("local.properties").add("security", "memoria");
		loader.file("producao.properties").add("security", "ldap");

		loader.file("memoria.properties").add("security.class", "MemoriaSecurity");
		loader.file("ldap.properties").add("security", "LdapSecurity");

		loader.file("local.memoria.properties").add("users", "usuario-${env}").add("env", "local").add("security", "memoria");
		loader.file("local.ldap.properties").add("ldap", "127.0.0.1").add("env", "local").add("security", "ldap");
		loader.file("producao.memoria.properties").add("users", "usuario-${env}").add("env", "producao").add("security", "memoria");
		loader.file("producao.ldap.properties").add("ldap", "ec2-ldap").add("env", "producao").add("security", "ldap");

		loader.file("external.properties").add("env", "producao");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${env}.properties");
		builder.addPropertyFile("${security}.properties");
		builder.addPropertyFile("${env}.${security}.properties");
		builder.addPropertyFile("external.properties");
		ConfigSource source = builder.build();

		// then
		assertEquals("producao", (String) source.get("env"));
		assertEquals("ldap", (String) source.get("security"));
		assertEquals("LdapSecurity", (String) source.get("security.class"));
		assertEquals("ec2-ldap", (String) source.get("ldap"));
		assertFalse(source.all().keySet().contains("users"));
	}

	@Test
	public void shouldResolveUltraComplexDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default.properties").add("env", "local");

		loader.file("local.properties").add("security", "memoria");
		loader.file("producao.properties").add("security", "ldap");

		loader.file("memoria.properties").add("security.class", "MemoriaSecurity");
		loader.file("ldap.properties").add("security", "LdapSecurity");

		loader.file("local.memoria.properties").add("users", "usuario-${env}").add("env", "local").add("security", "memoria");
		loader.file("local.ldap.properties").add("ldap", "127.0.0.1").add("env", "local").add("security", "ldap");
		loader.file("producao.memoria.properties").add("users", "usuario-${env}").add("env", "producao").add("security", "memoria");
		loader.file("producao.ldap.properties").add("ldap", "ec2-ldap").add("env", "producao").add("security", "ldap");

		loader.file("external.properties").add("env", "producao");

		loader.file("complex.local.properties");
		loader.file("complex.producao.properties").add("env", "local");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${env}.properties");
		builder.addPropertyFile("${security}.properties");
		builder.addPropertyFile("${env}.${security}.properties");
		builder.addPropertyFile("external.properties");
		builder.addPropertyFile("complex.${env}.properties");
		ConfigSource source = builder.build();

		// then
		assertEquals("producao", (String) source.get("env"));
		assertEquals("ldap", (String) source.get("security"));
		assertEquals("LdapSecurity", (String) source.get("security.class"));
		assertEquals("ec2-ldap", (String) source.get("ldap"));
		assertFalse(source.all().keySet().contains("users"));
	}

	@Test
	public void shouldResolveMegaUltraComplexDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default.properties").add("env", "local");

		loader.file("local.properties").add("security", "memoria").add("dbconfig", "h2");
		loader.file("producao.properties").add("security", "ldap").add("dbconfig", "mysql");

		loader.file("memoria.properties").add("security.class", "MemoriaSecurity");
		loader.file("ldap.properties").add("security", "LdapSecurity");

		loader.file("local.memoria.properties").add("users", "usuario-${env}").add("env", "local").add("security", "memoria");
		loader.file("local.ldap.properties").add("ldap", "127.0.0.1").add("env", "local").add("security", "ldap");
		loader.file("producao.memoria.properties").add("users", "usuario-${env}").add("env", "producao").add("security", "memoria");
		loader.file("producao.ldap.properties").add("ldap", "ec2-ldap").add("env", "producao").add("security", "ldap");

		loader.file("external.properties").add("env", "producao");

		loader.file("h2.properties").add("jpaconfig", "toplink");
		loader.file("mysql.properties").add("jpaconfig", "hibernate");

		loader.file("toplink.properties").add("jpa.packages", "test");
		loader.file("hibernate.properties").add("jpa.packages", "org");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${env}.properties");
		builder.addPropertyFile("${security}.properties");
		builder.addPropertyFile("${env}.${security}.properties");
		builder.addPropertyFile("external.properties");
		builder.addPropertyFile("${dbconfig}.properties");
		builder.addPropertyFile("${jpaconfig}.properties");

		ConfigSource source = builder.build();

		// then
		assertEquals("producao", (String) source.get("env"));
		assertEquals("ldap", (String) source.get("security"));
		assertEquals("LdapSecurity", (String) source.get("security.class"));
		assertEquals("ec2-ldap", (String) source.get("ldap"));
		assertEquals("mysql", (String) source.get("dbconfig"));
		assertEquals("hibernate", (String) source.get("jpaconfig"));
		assertEquals("org", (String) source.get("jpa.packages"));
		assertFalse(source.all().keySet().contains("users"));
	}

	@Test
	public void shouldResolveExtremeMegaUltraComplexDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default.properties").add("env", "local");

		loader.file("local.properties").add("security", "memoria").add("dbconfig", "h2");
		loader.file("producao.properties").add("security", "ldap").add("dbconfig", "mysql");

		loader.file("memoria.properties").add("security.class", "MemoriaSecurity");
		loader.file("ldap.properties").add("security", "LdapSecurity");

		loader.file("local.memoria.properties").add("users", "usuario-${env}").add("env", "local").add("security", "memoria");
		loader.file("local.ldap.properties").add("ldap", "127.0.0.1").add("env", "local").add("security", "ldap");
		loader.file("producao.memoria.properties").add("users", "usuario-${env}").add("env", "producao").add("security", "memoria");
		loader.file("producao.ldap.properties").add("ldap", "ec2-ldap").add("env", "producao").add("security", "ldap");

		loader.file("external.properties").add("env", "producao");

		loader.file("h2.properties").add("jpaconfig", "${env}.toplink");
		loader.file("mysql.properties").add("jpaconfig", "${env}.hibernate");

		loader.file("local.toplink.properties").add("jpa.packages", "test");
		loader.file("local.hibernate.properties").add("jpa.packages", "org");
		loader.file("producao.toplink.properties").add("jpa.packages", "prodtest");
		loader.file("producao.hibernate.properties").add("jpa.packages", "prodorg");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${env}.properties");
		builder.addPropertyFile("${security}.properties");
		builder.addPropertyFile("${env}.${security}.properties");
		builder.addPropertyFile("external.properties");
		builder.addPropertyFile("${dbconfig}.properties");
		builder.addPropertyFile("${jpaconfig}.properties");

		ConfigSource source = builder.build();

		// then
		assertEquals("producao", (String) source.get("env"));
		assertEquals("ldap", (String) source.get("security"));
		assertEquals("LdapSecurity", (String) source.get("security.class"));
		assertEquals("ec2-ldap", (String) source.get("ldap"));
		assertEquals("mysql", (String) source.get("dbconfig"));
		assertEquals("producao.hibernate", (String) source.get("jpaconfig"));
		assertEquals("prodorg", (String) source.get("jpa.packages"));
		assertFalse(source.all().keySet().contains("users"));
	}

	@Test
	public void shouldResolveVerySimpleDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default").add("a", "a");
		loader.file("a").add("b", "b");
		loader.file("b").add("c", "c");
		loader.file("c").add("a", "a2");
		loader.file("a2").add("b", "b2");
		loader.file("b2").add("b2", "true");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${a}.properties");
		builder.addPropertyFile("${b}.properties");
		builder.addPropertyFile("${c}.properties").optionalWhenMissingProperty("c");

		ConfigSource source = builder.build();

		// then
		assertEquals("a2", (String) source.get("a"));
		assertEquals("b2", (String) source.get("b"));
		assertEquals("true", (String) source.get("b2"));
		assertFalse(source.all().keySet().contains("c"));
	}

	@Test
	public void shouldResolveUltraSimpleDependencies() {
		// given
		PropertiesStreamLoader loader = new PropertiesStreamLoader();
		loader.file("default").add("a", "a");
		loader.file("a").add("b", "b");
		loader.file("b").add("c", "c");
		loader.file("c").add("a", "a2");
		loader.file("a2").add("b", "b2");
		loader.file("b2").add("b2", "true");
		loader.file("c2").add("c2", "true");
		loader.file("fixed").add("d", "d${b}");
		loader.file("db2").add("c", "c2");

		ConfigSourceBuilder builder = new ConfigSourceBuilder(loader);

		// when
		builder.addPropertyFile("default.properties");
		builder.addPropertyFile("${a}.properties");
		builder.addPropertyFile("${b}.properties");
		builder.addPropertyFile("${c}.properties").optionalWhenMissingProperty("c");
		builder.addPropertyFile("fixed.properties");
		builder.addPropertyFile("${d}.properties");

		ConfigSource source = builder.build();

		// then
		assertEquals("a2", (String) source.get("a"));
		assertEquals("b2", (String) source.get("b"));
		assertEquals("c2", (String) source.get("c"));
		assertEquals("db2", (String) source.get("d"));
		assertEquals("true", (String) source.get("b2"));
		assertEquals("true", (String) source.get("c2"));
		assertFalse(source.all().keySet().contains("c"));
	}

	@Test
	public void shouldAddSystemEnvProvider() {
		// given
		ConfigSourceBuilder builder = new ConfigSourceBuilder(new PropertiesStreamLoader());
		String firstEnvKey = System.getenv().keySet().iterator().next();

		// when
		builder.addSystemEnv();
		ConfigSource source = builder.build();

		// then
		assertNotNull(source.get(firstEnvKey));
	}

	public static class PropertiesStreamLoader implements StreamLoader {

		private Map<String, Properties> files = new HashMap<String, Properties>();

		protected Properties getOrCreate(String location) {
			Properties properties = files.get(location);
			if (properties == null) {
				properties = new Properties();
				files.put(location, properties);
			}
			return properties;
		}

		public InputStream get(String location) throws IOException {
			Properties properties = files.get(location);
			if (properties == null)
				return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			properties.store(baos, null);
			return new ByteArrayInputStream(baos.toByteArray());
		}

		public PropertyBuilder file(String location) {
			return new PropertyBuilder(getOrCreate(location));
		}

		private class PropertyBuilder {

			private Properties properties;

			public PropertyBuilder(Properties properties) {
				this.properties = properties;
			}

			public PropertyBuilder add(String key, String value) {
				properties.setProperty(key, value);
				return this;
			}

		}

	}
}
