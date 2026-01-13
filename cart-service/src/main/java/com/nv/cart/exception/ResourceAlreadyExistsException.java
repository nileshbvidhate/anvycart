package com.nv.cart.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
