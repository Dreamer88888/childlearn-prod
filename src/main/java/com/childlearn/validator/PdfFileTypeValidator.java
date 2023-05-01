package com.childlearn.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PdfFileTypeValidator implements ConstraintValidator<PdfFileTypeConstraint, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return isImageContentType(file.getContentType());
    }

    private boolean isImageContentType(String contentType) {
        return contentType.equals("application/pdf");
    }
}
