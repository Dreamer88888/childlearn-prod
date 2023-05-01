package com.childlearn.repository;

import com.childlearn.entity.AnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {

    Optional<AnswerSheet> findById(Long id);

    void deleteById(Long id);

    Optional<AnswerSheet> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    Optional<List<AnswerSheet>> findByAssignmentId(Long assignmentId);

}
