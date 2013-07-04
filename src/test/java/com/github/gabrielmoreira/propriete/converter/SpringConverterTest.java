package com.github.gabrielmoreira.propriete.converter;

import static org.junit.Assert.*;

import org.junit.Test;

public class SpringConverterTest extends SimpleConverterTest {

	@Override
	protected Converter getConverter() {
		return new SpringConverter();
	}

	@Test
	public void convertFromStringToBooleanTrue() throws Exception {
		// given/when/then
		assertEquals(Boolean.TRUE, getConverter().convert(" on ", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert(" true ", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert(" yes ", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert(" 1 ", Boolean.class));
	}

	@Test
	public void convertFromStringToBooleanFalse() throws Exception {
		// given/when/then
		assertEquals(Boolean.FALSE, getConverter().convert(" false ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" no ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" off ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" 0 ", Boolean.class));
	}

}
