package com.childlearn.controller;

import com.childlearn.dto.*;
import com.childlearn.entity.Class;
import com.childlearn.entity.*;
import com.childlearn.service.*;
import com.childlearn.util.GlobalFunction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportHeaderService reportHeaderService;

    @Autowired
    private ReportDetailService reportDetailService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    private static final String SUMMARY_NOTES = "Catatan baru bisa diisi setelah laporan semua mata pelajaran telah terisi.";

    // LIST STUDENT STELAH PILIH KELAS
    @GetMapping("/{classId}")
    public String getViewListStudent(@PathVariable Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<UserDisplayDto> students = new ArrayList<>();
        List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

        for (StudentDto studentDto : studentDtos) {
            String studentBase64 = java.util.Base64.getEncoder().encodeToString(studentDto.getUser().getFile());
            students.add(new UserDisplayDto(studentDto.getId(), studentDto.getUser().getFullName(), studentBase64));
        }

        model.addAttribute("students", students);

        return "list-report";
    }

    // SETELAH PILIH MURID
    @GetMapping
    public String getViewStudentReport(@RequestParam("studentId") Long studentId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("studentId", studentId);
        model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(studentId));

        return "student-report-header";
    }

    // LIHAT REPORT HEADER [ORTU]
    @GetMapping("/detail/{userId}")
    public String getViewReportDetail(@PathVariable("userId") Long pathUserId, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Student student = studentService.findByUserId(pathUserId);

        model.addAttribute("studentId", student.getId());
        model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(student.getId()));

        return "student-report-header";
    }

    // LIHAT DETAIL REPORT [GURU]
    @GetMapping("/detail/{studentId}/{reportHeaderId}")
    public String getViewReportDetail(@PathVariable("studentId") Long studentId, @PathVariable("reportHeaderId") Long reportHeaderId, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        model.addAttribute("reportDetail", reportDetailService.findDtoByStudentIdAndReportHeaderId(studentId, reportHeaderId));

        return "student-report";
    }

    // 1. GET VIEW MEMBUAT REPORT HEADER
    @GetMapping("/header/add/{studentId}")
    public String getViewAddReportHeader(@PathVariable(name = "studentId") Long studentId, HttpServletRequest request, HttpServletResponse response, Model model) throws JsonProcessingException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        // 1. Dapetin Id untuk link report header dan detail
        ReportHeader initialReportHeader = reportHeaderService.createReportHeader(new ReportHeader());
        Long reportHeaderId = initialReportHeader.getId();

        model.addAttribute("studentId", studentId);
        model.addAttribute("summary", SUMMARY_NOTES);

        model.addAttribute("reportHeaderId", reportHeaderId);

        StudentDto student = studentService.findById(studentId);
        List<MarkedSubjectDto> subjects = subjectService.initializeSubjectWithReportMark();
        ReportTempDto reportTempDto = new ReportTempDto(studentId, reportHeaderId, null, null, null, null);
        String rtdBase64 = serializeObject(reportTempDto);
        boolean isAllFilled = subjectService.checkIfAllSubjectIsFilled(subjects);

        model.addAttribute("reportRequestDto", new ReportRequestDto(reportHeaderId, student.getUser().getFullName(), SUMMARY_NOTES, studentId, null, isAllFilled, rtdBase64));
        model.addAttribute("rtdBase64", rtdBase64);
        model.addAttribute("subjects", subjects);
        model.addAttribute("isAllFilled", isAllFilled);

        return "add-report-header";
    }

    // 2. GET VIEW MENAMBAHKAN REPORT DETAIL
    @GetMapping("/detail/add/{studentId}/{reportHeaderId}/{subjectId}/{rtdBase64}")
    public String getViewAddReportDetail(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "reportHeaderId") Long reportHeaderId,
                                         @PathVariable(name = "subjectId") Long subjectId, @PathVariable(name = "rtdBase64") String rtdBase64,
                                         HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Subject subject = subjectService.findById(subjectId);

        ReportDetailRequestDto reportDetailRequestDto = new ReportDetailRequestDto(subject.getName(), subjectId, studentId, null, null, null, reportHeaderId, rtdBase64);

        model.addAttribute("reportDetailRequestDto", reportDetailRequestDto);

        return "add-report-detail";
    }

    // 3. ADD REPORT DETAIL
    @PostMapping("/detail/add/{studentId}/{reportHeaderId}/{subjectId}/{rtdBase64}")
    public String addReportDetail(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "reportHeaderId") Long reportHeaderId,
                                  @PathVariable(name = "subjectId") Long subjectId, @PathVariable(name = "rtdBase64") String rtdBase64,
                                  @Valid @ModelAttribute("reportDetailRequestDto") ReportDetailRequestDto reportDetailRequestDto,
                                  BindingResult bindingResult, HttpServletRequest request, Model model) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        ReportTempDto reportTempDto = deserializeObject(rtdBase64);

        if (!bindingResult.hasErrors()) {
            List<String> notes = new ArrayList<>();
            List<Integer> scores = new ArrayList<>();
            List<Long> subjectIds = new ArrayList<>();
            List<String> files = new ArrayList<>();

            if (reportTempDto.getNotes() != null) {
                notes = reportTempDto.getNotes();
                scores = reportTempDto.getScore();
                subjectIds = reportTempDto.getSubjectId();
                files = reportTempDto.getBase64();
            }

            notes.add(reportDetailRequestDto.getNotes());
            scores.add(reportDetailRequestDto.getScore());
            subjectIds.add(reportDetailRequestDto.getSubjectId());
            files.add(Base64Utils.encodeToString(reportDetailRequestDto.getFile().getBytes()));

            List<String> toBeAddNotes = notes;
            List<Integer> toBeAddScores = scores;
            List<Long> toBeAddSubjectIds = subjectIds;
            List<String> toBeAddFiles = files;

            ReportTempDto updatedReportTempDto = new ReportTempDto(studentId, reportHeaderId, toBeAddSubjectIds, toBeAddNotes, toBeAddScores, toBeAddFiles);

            String updatedRtdBase64 = serializeObject(updatedReportTempDto);
            StudentDto studentDto = studentService.findById(studentId);
            List<MarkedSubjectDto> subjects = subjectService.findSubjectWithReportMark(toBeAddSubjectIds);
            boolean isAllFilled = subjectService.checkIfAllSubjectIsFilled(subjects);
            String summary = "";
            if (isAllFilled) {
                summary = "";
            } else {
                summary = SUMMARY_NOTES;
            }

            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeaderId", reportHeaderId);
            model.addAttribute("reportRequestDto", new ReportRequestDto(reportHeaderId, studentDto.getUser().getFullName(), summary, studentId, null, isAllFilled, updatedRtdBase64));
            model.addAttribute("rtdBase64", updatedRtdBase64);
            model.addAttribute("subjects", subjects);
            model.addAttribute("isAllFilled", isAllFilled);
            model.addAttribute("message", "Sukses Menyimpan Data");

            return "add-report-header";
        } else {
            Subject subject = subjectService.findById(subjectId);

            model.addAttribute("reportDetailRequestDto", reportDetailRequestDto);
            model.addAttribute("subjectName", subject.getName());
            model.addAttribute("subjectId", subjectId);
            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeaderId", reportHeaderId);

            return "add-report-detail";
        }
    }

    // 4. MENAMBAHKAN REPORT HEADER
    @PostMapping("/header/add/{studentId}/{reportHeaderId}/{rtdBase64}")
    public String addReportHeader(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "reportHeaderId") Long reportHeaderId,
                                  @PathVariable(name = "rtdBase64") String rtdBase64,
                                  @Valid @ModelAttribute("reportRequestDto") ReportRequestDto reportRequestDto,
                                  BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        ReportTempDto reportTempDto = deserializeObject(rtdBase64);
        List<Long> subjectIds = reportTempDto.getSubjectId();

        if (!bindingResult.hasErrors()) {
            // UPDATE REPORT HEADER
            ReportHeader reportHeader = reportHeaderService.findById(reportHeaderId);

            reportHeader.setCreatedDate(new Date());
            reportHeader.setSummary(reportRequestDto.getSummary());
            reportHeader.setStudentId(studentId);
            reportHeader.setCreatedBy(fullName);

            reportHeaderService.updateReportHeader(reportHeader);

            // INSERT REPORT DETAILS


            for (int i = 0; i < reportTempDto.getSubjectId().size(); i++) {
                ReportDetail reportDetail = new ReportDetail();

                reportDetail.setReportHeaderId(reportHeaderId);
                reportDetail.setFile(Base64Utils.decodeFromString(reportTempDto.getBase64().get(i)));
                reportDetail.setNotes(reportTempDto.getNotes().get(i));
                reportDetail.setScore(reportTempDto.getScore().get(i));
                reportDetail.setSubjectId(reportTempDto.getSubjectId().get(i));

                reportDetailService.createReportDetail(reportDetail);
            }

            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(studentId));
            model.addAttribute("message", "Sukses Menambahkan Laporan Murid");

            return "student-report-header";
        } else {
            List<MarkedSubjectDto> subjects = subjectService.findSubjectWithReportMark(subjectIds);

            model.addAttribute("studentId", studentId);
            model.addAttribute("subjects", subjects);
            model.addAttribute("rtdBase64", rtdBase64);
            model.addAttribute("reportHeaderId", reportHeaderId);
            model.addAttribute("isAllFilled", true);
            model.addAttribute("reportRequestDto", reportRequestDto);

            return "add-report-header";
        }
    }

    @GetMapping("/header/update/{studentId}/{reportHeaderId}")
    public String getViewUpdateReportByStudentAndReportHeaderId(@PathVariable Long studentId, @PathVariable Long reportHeaderId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        StudentDto student = studentService.findById(studentId);
        ReportHeader reportHeader = reportHeaderService.findById(reportHeaderId);

        if (student != null && reportHeader != null) {
            List<Subject> subjects = subjectService.findAll();

            model.addAttribute("reportHeaderUpdateDto", new ReportHeaderUpdateDto(student.getUser().getFullName(), reportHeader.getSummary(), null, studentId, reportHeader.getId()));
            model.addAttribute("subjects", subjects);

            return "edit-report-header";
        } else {
            List<StudentDto> studentDtos = studentService.findAll().stream().map(studentDto -> {
                Class cl = classService.findById(studentDto.getClassId());
                User user = userService.findById(studentDto.getUserId());

                StudentDto newStudentDto = new StudentDto(studentDto.getId(), cl, user);

                return newStudentDto;
            }).collect(Collectors.toList());

            model.addAttribute("students", studentDtos);

            return "manage-student";
        }
    }

    @GetMapping("/detail/update/{studentId}/{reportHeaderId}/{subjectId}")
    private String getViewMappingEditReportDetail(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "reportHeaderId") Long reportHeaderId,
                                                  @PathVariable(name = "subjectId") Long subjectId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Subject subject = subjectService.findById(subjectId);
        ReportDetail reportDetail = reportDetailService.findByReportHeaderIdAndSubjectId(reportHeaderId, subjectId);

        ReportDetailUpdateDto reportDetailUpdateDto = new ReportDetailUpdateDto(subject.getName(), subjectId, studentId, reportDetail.getNotes(), reportDetail.getScore(), null, reportHeaderId, reportDetail.getId());

        model.addAttribute("reportDetailUpdateDto", reportDetailUpdateDto);

        return "edit-report-detail";
    }

    @PostMapping("/header/update")
    public String updateReportHeader(@Valid @ModelAttribute("reportHeaderUpdateDto") ReportHeaderUpdateDto reportHeaderUpdateDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            log.info("masuk");
            ReportHeader reportHeader = reportHeaderService.findById(reportHeaderUpdateDto.getReportHeaderId());

            reportHeader.setSummary(reportHeaderUpdateDto.getSummary());

            reportHeaderService.updateReportHeader(reportHeader);

            Long studentId = reportHeader.getStudentId();

            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(studentId));
            model.addAttribute("message", "Sukses Menyunting Laporan Murid");

            return "student-report-header";
        } else {
            List<Subject> subjects = subjectService.findAll();

            model.addAttribute("reportHeaderUpdateDto", reportHeaderUpdateDto);
            model.addAttribute("subjects", subjects);

            return "edit-report-header";
        }
    }

    @PostMapping("/detail/update/{studentId}/{reportHeaderId}")
    public String updateReportDetail(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "reportHeaderId") Long reportHeaderId,
                                     @Valid @ModelAttribute("reportDetailUpdateDto") ReportDetailUpdateDto reportDetailUpdateDto, BindingResult bindingResult,
                                     HttpServletRequest request, Model model) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            ReportDetail reportDetail = reportDetailService.findById(reportDetailUpdateDto.getReportDetailId());

            reportDetail.setScore(reportDetailUpdateDto.getScore());
            reportDetail.setNotes(reportDetailUpdateDto.getNotes());

            if (!reportDetailUpdateDto.getFile().isEmpty()) {
                reportDetail.setFile(reportDetailUpdateDto.getFile().getBytes());
            }

            reportDetailService.updateReportDetail(reportDetail);

            StudentDto student = studentService.findById(studentId);
            ReportHeader reportHeader = reportHeaderService.findById(reportHeaderId);

            if (student != null && reportHeader != null) {
                List<Subject> subjects = subjectService.findAll();

                model.addAttribute("reportHeaderUpdateDto", new ReportHeaderUpdateDto(student.getUser().getFullName(), reportHeader.getSummary(), null, studentId, reportHeader.getId()));
                model.addAttribute("subjects", subjects);
                model.addAttribute("message", "Sukses Menyimpan Data");

                return "edit-report-header";
            } 
            else {
                return "redirect:/";
            }
        } else {
            model.addAttribute("reportDetailUpdateDto", reportDetailUpdateDto);

            return "edit-report-detail";
        }

    }

    @GetMapping("/delete/{studentId}/{reportHeaderId}")
    public String deleteReportHeader(@PathVariable Long studentId, @PathVariable Long reportHeaderId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeleteSuccess = reportDetailService.deleteReportHeader(reportHeaderId);

        if (isDeleteSuccess) {
            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(studentId));

            model.addAttribute("message", "Sukses Menghapus Laporan Murid");
            return "student-report-header";
        } else {
            model.addAttribute("studentId", studentId);
            model.addAttribute("reportHeader", reportHeaderService.findDtoByStudentId(studentId));

            return "student-report-header";
        }
    }

    private String serializeObject(ReportTempDto reportTempDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedObject = null;
        try {
            serializedObject = objectMapper.writeValueAsString(reportTempDto);
        } catch (JsonProcessingException e) {
            log.error("ERROR SERIALIZE OBJECT: " + e.getMessage());
        }
        return encodeBase64(serializedObject);
    }

    private ReportTempDto deserializeObject(String serializedObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        serializedObject = decodeBase64(serializedObject);

        ReportTempDto reportTempDto = null;

        try {
            reportTempDto = objectMapper.readValue(serializedObject, ReportTempDto.class);
        } catch (JsonProcessingException e) {
            log.info("ERROR DESERIALIZED OBJECT: " + e.getMessage());
        }

        return reportTempDto;
    }

    private String encodeBase64(String toBeEncoded) {
        return Base64Utils.encodeToString(toBeEncoded.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeBase64(String tobeDecoded)  {
        byte[] bytes = Base64Utils.decodeFromString(tobeDecoded);
        return new String(bytes);
    }

}
