package com.project.triport.validation;

import com.project.triport.responseDto.ResponseDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleValidationExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseDto(false, errorMessage, 400);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseDto handleConstraintViolation(ConstraintViolationException exception) {
        exception.printStackTrace();
        return new ResponseDto(false, exception.getMessage(), 400);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseDto handleApiRequestException(IllegalArgumentException exception) {
        exception.printStackTrace();
        return new ResponseDto(false, exception.getMessage(), 400);
    }

    @ExceptionHandler(value = { IOException.class })
    public ResponseDto handleApiRequestException(IOException exception) {
        exception.printStackTrace();
        return new ResponseDto(false, exception.getMessage(), 500);
    }

    @ExceptionHandler(value = { MessagingException.class })
    public ResponseDto handleApiRequestException(MessagingException exception) {
        exception.printStackTrace();
        return new ResponseDto(false, exception.getMessage(), 400);
    }
}
