package com.childlearn.repository;

import com.childlearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByRole(String role);

}