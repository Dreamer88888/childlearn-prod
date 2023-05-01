package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReportImageFileTypeValidator.class)
public @interface ReportImageFileTypeConstraint {

    String message() default "Tipe file harus dalam format .png, .jpg, atau .jpeg";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
