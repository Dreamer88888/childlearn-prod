package com.childlearn.repository;

import com.childlearn.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    void deleteById(Long id);

    Optional<Position> findById(Long id);

    Optional<Position> findByName(String name);

}
