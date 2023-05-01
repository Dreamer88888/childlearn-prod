package com.childlearn.repository;

import com.childlearn.entity.AgendaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaDetailRepository extends JpaRepository<AgendaDetail, Long> {

    Optional<AgendaDetail> findById(Long id);

    void deleteById(Long id);

    List<AgendaDetail> findByAgendaHeaderId(Long agendaHeaderId);

}
