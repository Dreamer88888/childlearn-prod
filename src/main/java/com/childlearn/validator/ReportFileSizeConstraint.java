package com.childlearn.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReportFileSizeValidator.class)
public @interface ReportFileSizeConstraint {

    String message() default "File tidak boleh kosong dan tidak boleh lebih dari 8 MB";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
    long maxSizeInMB() default 8;

}
