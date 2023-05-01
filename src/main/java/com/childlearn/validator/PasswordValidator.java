package com.childlearn.validator;

import com.childlearn.dto.ManagePasswordDto;
import com.childlearn.entity.User;
import com.childlearn.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, ManagePasswordDto> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(ManagePasswordDto managePasswordDto, ConstraintValidatorContext constraintValidatorContext) {
        if (isCurrentPasswordLegit(managePasswordDto.getUserId(), managePasswordDto.getCurrentPassword())) {
            return true;
        } else {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("currentPassword").addConstraintViolation();

            return false;
        }
    }

    private boolean isCurrentPasswordLegit(Long userId, String currentPassword) {
        User user = userService.findById(userId);

        return user.getPassword().equals(currentPassword);
    }

}
