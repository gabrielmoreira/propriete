package com.github.gabrielmoreira.propriete.builder;

import java.io.IOException;
import java.io.InputStream;

public interface StreamLoader {

	InputStream get(String location) throws IOException;

}
