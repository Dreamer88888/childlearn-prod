package com.childlearn.repository;

import com.childlearn.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Optional<Assignment> findById(Long id);

    void deleteById(Long id);

    List<Assignment> findBySubjectId(Long id);

    List<Assignment> findBySubjectIdAndClassId(Long subjectId, Long classId);

}
