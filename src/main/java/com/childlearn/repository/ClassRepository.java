package com.childlearn.repository;

import com.childlearn.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    void deleteById(Long id);

    Optional<Class> findById(Long id);
    Object findNameById(Long id);

}
