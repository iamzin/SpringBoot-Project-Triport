package com.project.triport.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException {
    private final Boolean ok;
    private final String msg;
    private final String status;
}
