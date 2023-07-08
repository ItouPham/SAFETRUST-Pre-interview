package com.safetrust.preinterview.exception;

public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5215820811465866815L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
	
}
