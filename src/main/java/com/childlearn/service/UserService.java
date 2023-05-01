package com.childlearn.service;

import com.childlearn.dto.CredentialDto;
import com.childlearn.dto.UserDto;
import com.childlearn.entity.User;
import com.childlearn.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public List<User> findByRoleUser() {
        return userRepository.findByRole("USER");
    }

    public String findBase64ByUserId(Long userId) {
        User user = findById(userId);

        return Base64.getEncoder().encodeToString(user.getFile());
    }

    public User findByRoleHeadMaster() {
        List<User> user = userRepository.findByRole("HEADMASTER");
        if (!user.isEmpty()) {
            return user.get(0);
        } else {
            throw new NotFoundException("Head Master not found, create a new one");
        }
    }

    public User createUser(User user) {
        String generatedUsername = generateUsername(user.getFullName());
        String generatedPassword = generatePassword(user.getDob());

        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);
        user.setRole("USER");

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Long id = user.getId();
        Optional<User> updatedUser = userRepository.findById(id);
        if (updatedUser.isPresent()) {
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public Optional<User> validateCredential(CredentialDto credentialDto) throws UserPrincipalNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndPassword(credentialDto.getUsername(), credentialDto.getPassword());
        return user;
    }

    private String generateUsername(String fullName) {
        fullName = fullName.toLowerCase();
        List<String> names = List.of(fullName.split(" "));
        String firstName = names.get(0);
        String lastName = "";
        if (names.size() > 1) {
            lastName = names.get(names.size() - 1);
        }
        String thisYear = Year.now().toString();
        String twoYearsLater = Year.now().plusYears(2).toString();

        String entryYear = thisYear.substring(thisYear.length() - 2);
        String graduationYear = twoYearsLater.substring(twoYearsLater.length() - 2);

        String generatedUsername = firstName + lastName + entryYear + graduationYear;

        Integer counter = 1;

        while(userRepository.findByUsername(generatedUsername).isPresent()) {
            generatedUsername = firstName + lastName + counter.toString() + entryYear + graduationYear;
            if (userRepository.findByUsername(generatedUsername).isEmpty()) {
                return generatedUsername;
            } else {
                counter++;
            }
        }

        return generatedUsername;
    }

    private String generatePassword(Date dob) {
        String pattern = "MM-dd-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String generatedPassword = simpleDateFormat.format(dob);

        generatedPassword = generatedPassword.replaceAll("-", "");

        return generatedPassword;
    }
}
