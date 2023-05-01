package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CredentialValidator.class)
public @interface CredentialConstraint {

    String message() default "Username dan password harus sesuai";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
