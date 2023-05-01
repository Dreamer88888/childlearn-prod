package com.childlearn.repository;

import com.childlearn.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findById(Long id);

    void deleteById(Long id);

    Optional<List<Teacher>> findByPositionId(Long positionId);

    Optional<List<Teacher>> findByPositionIdNotIn(List<Long> positionIds);

    Optional<Teacher> findByUserId(Long userId);

}

