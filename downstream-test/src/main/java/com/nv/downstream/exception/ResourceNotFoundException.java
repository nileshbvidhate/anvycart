package com.nv.downstream.exception;

public class ResourceNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = -6460860207007076126L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
