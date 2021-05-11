package com.project.triport.validation;

import com.project.triport.requestDto.MemberRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PwdCheckValidator implements ConstraintValidator<PwdCheck, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.equals(new MemberRequestDto().getPassword().toString());
    }
}
