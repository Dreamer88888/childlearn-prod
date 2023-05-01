package com.childlearn.service;

import com.childlearn.dto.ReportHeaderDisplayDto;
import com.childlearn.dto.StudentDto;
import com.childlearn.entity.ReportDetail;
import com.childlearn.entity.ReportHeader;
import com.childlearn.repository.ReportHeaderRepository;
import com.childlearn.util.GlobalFunction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportHeaderService {

    @Autowired
    private ReportHeaderRepository reportHeaderRepository;

    @Autowired
    private StudentService studentService;

    public List<ReportHeader> findAll() {
        return reportHeaderRepository.findAll();
    }

    public ReportHeader findById(Long id) {
        Optional<ReportHeader> reportHeader = reportHeaderRepository.findById(id);
        if (reportHeader.isPresent()) {
            return reportHeader.get();
        } else {
            return null;
        }
    }

    public ReportHeaderDisplayDto findDtoByStudentId(Long studentId) {
        StudentDto studentDto = studentService.findById(studentId);
        String base64 = Base64.getEncoder().encodeToString(studentDto.getUser().getFile());

        ReportHeaderDisplayDto reportHeaderDisplayDto = new ReportHeaderDisplayDto();

        reportHeaderDisplayDto.setFullName(studentDto.getUser().getFullName());
        reportHeaderDisplayDto.setBase64(base64);
        reportHeaderDisplayDto.setClassName(studentDto.getCl().getName());

        List<ReportHeader> reportHeaders = findByStudentId(studentId);

        List<String> createdDates = new ArrayList<>();
        List<String> summaries = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (ReportHeader reportHeader : reportHeaders) {
            createdDates.add(GlobalFunction.convertLocaleIndonesiaDate(reportHeader.getCreatedDate()));
            summaries.add(reportHeader.getSummary());
            ids.add(reportHeader.getId());
        }

        reportHeaderDisplayDto.setCreatedDate(createdDates);
        reportHeaderDisplayDto.setSummary(summaries);
        reportHeaderDisplayDto.setReportHeaderIds(ids);

        return reportHeaderDisplayDto;
    }

    public List<ReportHeader> findByStudentId(Long studentId) {
        return reportHeaderRepository.findByStudentId(studentId);
    }

    public ReportHeader findByStudentIdAndCreatedDate(Long studentId, Date createdDate) {
        Optional<ReportHeader> reportHeader = reportHeaderRepository.findByStudentIdAndCreatedDate(studentId, createdDate);
        if (reportHeader.isPresent()) {
            return reportHeader.get();
        } else {
            throw new NotFoundException("Report Header with studentId: " + studentId.toString() + " and createdDate: " + createdDate.toString() + " not found.");
        }
    }

    public ReportHeader createReportHeader(ReportHeader reportHeader) {
        return reportHeaderRepository.save(reportHeader);
    }

    public ReportHeader updateReportHeader(ReportHeader reportHeader) {
        Long id = reportHeader.getId();
        Optional<ReportHeader> updatedReportHeader = reportHeaderRepository.findById(id);
        if (updatedReportHeader.isPresent()) {
            return reportHeaderRepository.save(reportHeader);
        } else {
            return null;
        }
    }

    public boolean deleteById(Long reportHeaderId) {
        ReportHeader reportHeader = findById(reportHeaderId);

        if (reportHeader != null) {
            reportHeaderRepository.deleteById(reportHeaderId);

            return true;
        } else {
            return false;
        }
    }


}
