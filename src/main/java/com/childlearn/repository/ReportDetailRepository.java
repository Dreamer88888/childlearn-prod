package com.childlearn.repository;

import com.childlearn.entity.ReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {

    Optional<ReportDetail> findById(Long id);

    void deleteById(Long id);

    Optional<ReportDetail> findByReportHeaderIdAndSubjectId(Long reportHeaderId, Long subjectId);

    Optional<List<ReportDetail>> findByReportHeaderId(Long reportHeaderId);

}
