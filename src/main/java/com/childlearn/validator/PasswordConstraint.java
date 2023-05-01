package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordConstraint {

    String message() default "Kata sandi saat ini salah";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
