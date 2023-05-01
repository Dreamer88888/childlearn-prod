package com.childlearn.repository;

import com.childlearn.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    void deleteById(Long id);

    Optional<Subject> findById(Long id);

}
