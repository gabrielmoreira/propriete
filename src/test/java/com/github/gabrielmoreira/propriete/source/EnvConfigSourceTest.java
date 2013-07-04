package com.github.gabrielmoreira.propriete.source;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

public class EnvConfigSourceTest {

	@Test
	public void shouldGetEnvProperties() {
		// given
		SystemEnvConfigSource systemEnvConfigSource = new SystemEnvConfigSource();
		// when
		Object path = SystemUtils.IS_OS_WINDOWS ? systemEnvConfigSource.get("USERNAME") : systemEnvConfigSource.get("USER");
		// then
		assertNotNull(path);
	}
}
