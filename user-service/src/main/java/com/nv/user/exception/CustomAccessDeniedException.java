package com.nv.user.exception;

public class CustomAccessDeniedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CustomAccessDeniedException(String message) {
		super(message);
	}
	
}
