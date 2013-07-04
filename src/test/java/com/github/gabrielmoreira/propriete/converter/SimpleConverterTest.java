package com.github.gabrielmoreira.propriete.converter;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class SimpleConverterTest {

	@Test
	public void convertFromStringToString() throws Exception {
		// given
		String value = "132";
		// when
		String result = (String) getConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromStringToBigInteger() throws Exception {
		// given
		String value = "132";
		// when
		BigInteger result = (BigInteger) getConverter().convert(value, BigInteger.class);
		// then
		assertEquals(new BigInteger("132"), result);
	}

	@Test
	public void convertFromStringToBigDecimal() throws Exception {
		// given
		String value = "1.32";
		// when
		BigDecimal result = (BigDecimal) getConverter().convert(value, BigDecimal.class);
		// then
		assertEquals(new BigDecimal("1.32"), result);
	}

	@Test
	public void convertFromStringToLong() throws Exception {
		// given
		String value = "132";
		// when
		Long result = (Long) getConverter().convert(value, Long.class);
		// then
		assertEquals(new Long("132"), result);
	}

	@Test
	public void convertFromStringToDouble() throws Exception {
		// given
		String value = "1.32";
		// when
		Double result = (Double) getConverter().convert(value, Double.class);
		// then
		assertEquals(new Double("1.32"), result);
	}

	@Test
	public void convertFromStringToInteger() throws Exception {
		// given
		String value = "132";
		// when
		Integer result = (Integer) getConverter().convert(value, Integer.class);
		// then
		assertEquals(new Integer("132"), result);
	}

	@Test
	public void convertFromStringToPrimitiveInt() throws Exception {
		// given
		String value = "132";
		// when
		int result = (Integer) getConverter().convert(value, Integer.TYPE);
		// then
		assertEquals(132, result);
	}

	@Test
	public void convertFromStringToFloat() throws Exception {
		// given
		String value = "1.32";
		// when
		Float result = (Float) getConverter().convert(value, Float.class);
		// then
		assertEquals(new Float("1.32"), result);
	}

	@Test
	public void convertFromLongToString() throws Exception {
		// given
		long value = 132;
		// when
		String result = (String) getConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromBigDecimalToString() throws Exception {
		// given
		BigDecimal value = new BigDecimal("132");
		// when
		String result = (String) getConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromLongToBigDecimal() throws Exception {
		// given
		long value = 132;
		// when
		BigDecimal result = (BigDecimal) getConverter().convert(value, BigDecimal.class);
		// then
		assertEquals(new BigDecimal("132"), result);
	}

	@Test
	public void convertFromStringToBooleanTrue() throws Exception {
		// given/when/then
		assertEquals(Boolean.TRUE, getConverter().convert("1", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert("y", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert("yes", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert("ok", Boolean.class));
		assertEquals(Boolean.TRUE, getConverter().convert(" any string ", Boolean.class));
	}

	@Test
	public void convertFromStringToBooleanFalse() throws Exception {
		// given/when/then
		assertEquals(Boolean.FALSE, getConverter().convert("0 ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" n ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" no ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" off ", Boolean.class));
		assertEquals(Boolean.FALSE, getConverter().convert(" false ", Boolean.class));
	}

	@Test
	public void convertFromStringToBooleanNull() throws Exception {
		assertNull(getConverter().convert("  ", Boolean.class));
	}

	protected Converter getConverter() {
		return new SimpleConverter();
	}

}
