package com.vision.api.exception;

public class DuplicatedEntityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public DuplicatedEntityException(String message) {
        super(message);
    }
}