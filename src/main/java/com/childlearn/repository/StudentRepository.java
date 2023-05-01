package com.childlearn.repository;

import com.childlearn.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    void deleteById(Long id);

    Optional<Student> findById(Long id);

    List<Student> findByClassId(Long id);

    Optional<Student> findByUserId(Long id);

}