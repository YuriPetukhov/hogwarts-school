package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class ElementNotAcceptableException extends RuntimeException {
    public ElementNotAcceptableException(String message) {
        super(message);
    }
}
