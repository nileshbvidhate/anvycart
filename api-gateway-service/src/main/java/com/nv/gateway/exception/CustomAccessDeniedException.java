package com.nv.gateway.exception;

public class CustomAccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = 2221193092360342558L;

	public CustomAccessDeniedException(String msg) {
		super(msg);
	}
}
