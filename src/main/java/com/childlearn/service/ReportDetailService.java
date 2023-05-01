package com.childlearn.service;

import  com.childlearn.dto.ReportDetailDisplayDto;
import com.childlearn.dto.StudentDto;
import com.childlearn.entity.ReportDetail;
import com.childlearn.entity.ReportHeader;
import com.childlearn.entity.Subject;
import com.childlearn.repository.ReportDetailRepository;
import com.childlearn.util.GlobalFunction;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportDetailService {

    @Autowired
    private ReportDetailRepository reportDetailRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportHeaderService reportHeaderService;

    @Autowired
    private SubjectService subjectService;

    public List<ReportDetail> findAll() {
        return reportDetailRepository.findAll();
    }

    public ReportDetail findById(Long id) {
        Optional<ReportDetail> reportDetail = reportDetailRepository.findById(id);
        if (reportDetail.isPresent()) {
            return reportDetail.get();
        } else {
            return null;
        }
    }

    public ReportDetailDisplayDto findDtoByStudentIdAndReportHeaderId(Long studentId, Long reportHeaderId) {
        StudentDto studentDto = studentService.findById(studentId);
        ReportHeader reportHeader = reportHeaderService.findById(reportHeaderId);

        ReportDetailDisplayDto reportDetailDisplayDto = new ReportDetailDisplayDto();

        String b64 = userService.findBase64ByUserId(studentDto.getUser().getId());

        reportDetailDisplayDto.setBase64(b64);
        reportDetailDisplayDto.setFullName(studentDto.getUser().getFullName());
        reportDetailDisplayDto.setCreatedBy(reportHeader.getCreatedBy());
        reportDetailDisplayDto.setSummary(reportHeader.getSummary());
        reportDetailDisplayDto.setCreatedDate(GlobalFunction.convertLocaleIndonesiaDate(reportHeader.getCreatedDate()));

        List<String> subjectNames = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<String> notes = new ArrayList<>();
        List<String> reportBase64 = new ArrayList<>();

        List<ReportDetail> reportDetails = findByReportHeaderId(reportHeaderId);

        for (ReportDetail reportDetail : reportDetails) {
            Subject subject = subjectService.findById(reportDetail.getSubjectId());
            subjectNames.add(subject.getName());
            scores.add(reportDetail.getScore());
            notes.add(reportDetail.getNotes());

            String base64 = "";

            if (reportDetail.getFile() != null) {
                base64 = Base64Utils.encodeToString(reportDetail.getFile());
            }

            reportBase64.add(base64);
        }

        reportDetailDisplayDto.setSubjectNames(subjectNames);
        reportDetailDisplayDto.setScores(scores);
        reportDetailDisplayDto.setNotes(notes);
        reportDetailDisplayDto.setReportBase64(reportBase64);

        return reportDetailDisplayDto;
    }

    public ReportDetail findByReportHeaderIdAndSubjectId(Long reportHeaderId, Long subjectId) {
        Optional<ReportDetail> reportDetail = reportDetailRepository.findByReportHeaderIdAndSubjectId(reportHeaderId, subjectId);
        if (reportDetail.isPresent()) {
            return reportDetail.get();
        } else {
            return null;
        }
    }

    public ReportDetail createReportDetail(ReportDetail reportDetail) {
        return reportDetailRepository.save(reportDetail);
    }

    public ReportDetail updateReportDetail(ReportDetail reportDetail) {
        Optional<ReportDetail> updatedReportDetail = reportDetailRepository.findById(reportDetail.getId());
        if (updatedReportDetail.isPresent()) {
            return reportDetailRepository.save(reportDetail);
        } else {
            return null;
        }
    }

    public boolean deleteReportDetail(Long id) {
        Optional<ReportDetail> deletedReportDetail = reportDetailRepository.findById(id);
        if (deletedReportDetail.isPresent()) {
            reportDetailRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean deleteReportHeader(Long reportHeaderId) {
        ReportHeader deletedReportHeader = reportHeaderService.findById(reportHeaderId);
        if (deletedReportHeader != null) {
            List<ReportDetail> reportDetails = findByReportHeaderId(reportHeaderId);
            if (!reportDetails.isEmpty()) {
                for (ReportDetail reportDetail : reportDetails) {
                    deleteReportDetail(reportDetail.getId());
                }
            }
            reportHeaderService.deleteById(reportHeaderId);
            return true;
        } else {
            return false;
        }
    }

    public List<ReportDetail> findByReportHeaderId(Long reportHeaderId) {
        Optional<List<ReportDetail>> reportDetails = reportDetailRepository.findByReportHeaderId(reportHeaderId);
        if (reportDetails.isPresent()) {
            return reportDetails.get();
        } else {
            return null;
        }
    }

}
