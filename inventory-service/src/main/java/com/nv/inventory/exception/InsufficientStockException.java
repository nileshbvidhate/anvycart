package com.nv.inventory.exception;

public class InsufficientStockException extends RuntimeException {
	
    private static final long serialVersionUID = 8227137362554090990L;

	public InsufficientStockException(String message) {
        super(message);
    }
}
