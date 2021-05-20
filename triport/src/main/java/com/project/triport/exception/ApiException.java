package com.project.triport.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiException {
    private final Boolean ok;
    private final String msg;
    private final String status;
}
