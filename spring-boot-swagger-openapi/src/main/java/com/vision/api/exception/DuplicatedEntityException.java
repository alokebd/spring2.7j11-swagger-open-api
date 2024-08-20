package com.vision.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicatedEntityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public DuplicatedEntityException() {
        super();
    }

    public DuplicatedEntityException(String message) {
        super(message);
    }
}