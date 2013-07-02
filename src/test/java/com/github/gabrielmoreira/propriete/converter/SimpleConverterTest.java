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
		String result = (String) new SimpleConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromStringToBigInteger() throws Exception {
		// given
		String value = "132";
		// when
		BigInteger result = (BigInteger) new SimpleConverter().convert(value, BigInteger.class);
		// then
		assertEquals(new BigInteger("132"), result);
	}

	@Test
	public void convertFromStringToBigDecimal() throws Exception {
		// given
		String value = "1.32";
		// when
		BigDecimal result = (BigDecimal) new SimpleConverter().convert(value, BigDecimal.class);
		// then
		assertEquals(new BigDecimal("1.32"), result);
	}

	@Test
	public void convertFromStringToLong() throws Exception {
		// given
		String value = "132";
		// when
		Long result = (Long) new SimpleConverter().convert(value, Long.class);
		// then
		assertEquals(new Long("132"), result);
	}

	@Test
	public void convertFromStringToDouble() throws Exception {
		// given
		String value = "1.32";
		// when
		Double result = (Double) new SimpleConverter().convert(value, Double.class);
		// then
		assertEquals(new Double("1.32"), result);
	}

	@Test
	public void convertFromStringToInteger() throws Exception {
		// given
		String value = "132";
		// when
		Integer result = (Integer) new SimpleConverter().convert(value, Integer.class);
		// then
		assertEquals(new Integer("132"), result);
	}

	@Test
	public void convertFromStringToPrimitiveInt() throws Exception {
		// given
		String value = "132";
		// when
		int result = (Integer) new SimpleConverter().convert(value, Integer.TYPE);
		// then
		assertEquals(132, result);
	}

	@Test
	public void convertFromStringToFloat() throws Exception {
		// given
		String value = "1.32";
		// when
		Float result = (Float) new SimpleConverter().convert(value, Float.class);
		// then
		assertEquals(new Float("1.32"), result);
	}

	@Test
	public void convertFromLongToString() throws Exception {
		// given
		long value = 132;
		// when
		String result = (String) new SimpleConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromBigDecimalToString() throws Exception {
		// given
		BigDecimal value = new BigDecimal("132");
		// when
		String result = (String) new SimpleConverter().convert(value, String.class);
		// then
		assertEquals(new String("132"), result);
	}

	@Test
	public void convertFromLongToBigDecimal() throws Exception {
		// given
		long value = 132;
		// when
		BigDecimal result = (BigDecimal) new SimpleConverter().convert(value, BigDecimal.class);
		// then
		assertEquals(new BigDecimal("132"), result);
	}

	@Test
	public void convertFromStringToBooleanTrue() throws Exception {
		// given/when/then
		assertEquals(Boolean.TRUE, new SimpleConverter().convert("0.0", Boolean.class));
		assertEquals(Boolean.TRUE, new SimpleConverter().convert("1", Boolean.class));
		assertEquals(Boolean.TRUE, new SimpleConverter().convert("a", Boolean.class));
		assertEquals(Boolean.TRUE, new SimpleConverter().convert("yes", Boolean.class));
	}

	@Test
	public void convertFromStringToBooleanFalse() throws Exception {
		// given/when/then
		assertEquals(Boolean.FALSE, new SimpleConverter().convert("0 ", Boolean.class));
		assertEquals(Boolean.FALSE, new SimpleConverter().convert(" n ", Boolean.class));
		assertEquals(Boolean.FALSE, new SimpleConverter().convert(" no ", Boolean.class));
		assertEquals(Boolean.FALSE, new SimpleConverter().convert(" false ", Boolean.class));
	}

}
