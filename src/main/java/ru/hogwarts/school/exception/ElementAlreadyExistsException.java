package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ElementAlreadyExistsException extends RuntimeException {
    public ElementAlreadyExistsException(String message) {
        super(message);
    }
}
