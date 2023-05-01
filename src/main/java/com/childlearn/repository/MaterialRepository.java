package com.childlearn.repository;

import com.childlearn.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findById(Long id);

    void deleteById(Long id);

    List<Material> findBySubjectId(Long id);

    List<Material> findByClassIdAndSubjectId(Long classId, Long subjectId);

}
