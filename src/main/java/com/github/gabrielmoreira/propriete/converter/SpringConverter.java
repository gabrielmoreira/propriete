package com.github.gabrielmoreira.propriete.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;


public class SpringConverter implements Converter {

	private ConversionService conversionService;

	public SpringConverter() {
		this(new DefaultConversionService());
	}

	public SpringConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Object convert(Object source, Class<?> toType) {
		return conversionService.convert(source, toType);
	}

}
