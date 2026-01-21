package com.nv.payment.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	
    private static final long serialVersionUID = -8226085711278052468L;

	public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
