package com.project.triport.validation;

import com.project.triport.exception.ApiException;
import com.project.triport.exception.ApiRequestException;
import com.project.triport.responseDto.ResponseDto;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// @Valid로 인해 발생하는 Exception Handling
@RestControllerAdvice // RestController에서
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        // valid에 실패할 경우 해당 객체에서 설정한 error message를 retrun 하도록 설정
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            // String fieldName = ((FieldError) error).getField(); -> error가 발생하는 field명을 get 할 수 있다.
            String errorMessage = error.getDefaultMessage();
            errors.put("message", errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseDto handleApiRequestException(IllegalArgumentException ex) {
        ex.printStackTrace();
        return new ResponseDto(false, ex.getMessage(), 400);
    }

    @ExceptionHandler(value = { IOException.class })
    public ResponseDto handleApiRequestException(IOException ex) {
        ex.printStackTrace();
        return new ResponseDto(false, ex.getMessage(), 500);
    }
}
