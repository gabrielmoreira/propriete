package com.github.gabrielmoreira.propriete.visitor;

public interface PropertyVisitor {

	public void visit(String key, Object value);

}
