package com.nv.product.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6850381814667362913L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
