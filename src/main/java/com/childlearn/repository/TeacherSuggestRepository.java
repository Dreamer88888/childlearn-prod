package com.childlearn.repository;

import com.childlearn.entity.TeacherSuggest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherSuggestRepository extends JpaRepository<TeacherSuggest, Long> {

    Optional<TeacherSuggest> findById(Long id);

    void deleteById(Long id);

}
