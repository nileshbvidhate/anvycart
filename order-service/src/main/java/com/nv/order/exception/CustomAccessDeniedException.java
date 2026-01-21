package com.nv.order.exception;

public class CustomAccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = -8488791312238291151L;

	public CustomAccessDeniedException(String message)
	{
		super(message);
	}
}
