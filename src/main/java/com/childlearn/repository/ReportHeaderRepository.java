package com.childlearn.repository;

import com.childlearn.entity.ReportHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportHeaderRepository extends JpaRepository<ReportHeader, Long> {

    Optional<ReportHeader> findById(Long id);

    void deleteById(Long id);

    List<ReportHeader> findByStudentId(Long studentId);

    Optional<ReportHeader> findByStudentIdAndCreatedDate(Long studentId, Date createdDate);

}
