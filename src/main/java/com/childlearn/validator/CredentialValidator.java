package com.childlearn.validator;

import com.childlearn.dto.CredentialDto;
import com.childlearn.entity.User;
import com.childlearn.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Component
@Slf4j
public class CredentialValidator implements ConstraintValidator<CredentialConstraint, CredentialDto> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(CredentialDto credentialDto, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Optional<User> user = userService.validateCredential(credentialDto);
            if (user.isPresent()) {
                return true;
            } else {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                                .addPropertyNode("password").addConstraintViolation();

                return false;
            }
        } catch (UserPrincipalNotFoundException e) {
            log.info("USER NOT FOUND: " + e.getMessage());
        }
        return false;
    }

}
