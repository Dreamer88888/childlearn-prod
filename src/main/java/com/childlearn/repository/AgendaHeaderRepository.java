package com.childlearn.repository;

import com.childlearn.entity.AgendaHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaHeaderRepository extends JpaRepository<AgendaHeader, Long> {

    Optional<AgendaHeader> findById(Long id);

    void deleteById(Long id);

    Optional<List<AgendaHeader>> findByClassId(Long classId);

}
