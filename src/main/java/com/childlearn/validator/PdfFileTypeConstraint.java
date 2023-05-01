package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PdfFileTypeValidator.class)
public @interface PdfFileTypeConstraint {

    String message() default "Tipe file harus dalam format .pdf";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
