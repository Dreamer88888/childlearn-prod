package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatchConstraint {

    String message() default "Kata sandi baru dan konfirmasi harus sama";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
