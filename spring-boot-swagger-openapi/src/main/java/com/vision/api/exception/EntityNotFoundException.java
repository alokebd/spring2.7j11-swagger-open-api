package com.vision.api.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {super(); }

    public EntityNotFoundException(String message) {
        super(message);
    }
}