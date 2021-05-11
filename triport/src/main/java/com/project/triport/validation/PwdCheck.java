package com.project.triport.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PwdCheckValidator.class)
public @interface PwdCheck {
    String message() default "비밀번호가 일치하지 않습니다.";
    Class[] groups() default  {};
    Class[] payload() default {};
}
