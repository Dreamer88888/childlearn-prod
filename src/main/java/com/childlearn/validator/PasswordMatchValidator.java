package com.childlearn.validator;

import com.childlearn.dto.ManagePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatchConstraint, ManagePasswordDto> {

    @Override
    public boolean isValid(ManagePasswordDto managePasswordDto, ConstraintValidatorContext constraintValidatorContext) {
        if (managePasswordDto.getNewPassword().equals(managePasswordDto.getConfirmNewPassword())) {
            return true;
        } else {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmNewPassword").addConstraintViolation();

            return false;
        }
    }

}
