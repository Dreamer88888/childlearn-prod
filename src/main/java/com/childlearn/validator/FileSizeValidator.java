package com.childlearn.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSizeValidator implements ConstraintValidator<FileSizeConstraint, MultipartFile> {

    private static final Integer MB = 1024*1024;
    private long maxSizeInMB;

    @Override
    public void initialize(FileSizeConstraint constraintAnnotation) {
        this.maxSizeInMB = constraintAnnotation.maxSizeInMB();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.getSize() == 0L) return false;
        return file.getSize() <= maxSizeInMB * MB;
    }
}
