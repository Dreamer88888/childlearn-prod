package com.childlearn.controller;


import com.childlearn.dto.AnswerSheetRequestDto;
import com.childlearn.dto.AssignmentDto;
import com.childlearn.dto.StudentAsgDto;
import com.childlearn.dto.StudentDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.*;
import com.childlearn.entity.Class;
import com.childlearn.service.*;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/answer-sheet")
public class AnswerSheetController {

    @Autowired
    private AnswerSheetService answerSheetService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String getViewAddAnswerSheet(@RequestParam("assignmentId") Long assignmentId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Assignment assignment = assignmentService.findById(assignmentId);
        Subject subject = subjectService.findById(assignment.getSubjectId());
        Class cl = classService.findById(assignment.getClassId());

        model.addAttribute("answerSheetRequestDto", new AnswerSheetRequestDto(null, assignmentId));
        model.addAttribute("subjectName", subject.getName());
        model.addAttribute("className", cl.getName());
        model.addAttribute("deadline", assignment.getDeadline());

        return "add-answer-sheet";
    }

    @PostMapping("/add")
    public String addAnswerSheet(@Valid @ModelAttribute("answerSheetRequestDto") AnswerSheetRequestDto answerSheetRequestDto, BindingResult bindingResult, Model model, HttpServletRequest request) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            Assignment assignmentFromDB = assignmentService.findById(answerSheetRequestDto.getAssignmentId());
            Subject subject = subjectService.findById(assignmentFromDB.getSubjectId());
            if (subject != null) {
                Student student = studentService.findByUserId(userId);
                AnswerSheet answerSheet = new AnswerSheet();

                answerSheet.setStudentId(student.getId());
                answerSheet.setAssignmentId(assignmentFromDB.getId());
                answerSheet.setScore(-1);
                answerSheet.setUploadedDate(new Date());
                answerSheet.setFile(answerSheetRequestDto.getFile().getBytes());

                AnswerSheet answerSheetFromDB = answerSheetService.findByAssignmentIdAndStudentId(assignmentFromDB.getId(), student.getId());

                if (answerSheetFromDB != null) {
                    answerSheet = answerSheetService.updateAnswerSheet(answerSheetFromDB);
                } else {
                    answerSheet = answerSheetService.createAnswerSheet(answerSheet);
                }

                AnswerSheet finalAnswerSheet = answerSheet;
//                assignmentFromDB.setAnswerSheetId(finalAnswerSheet.getId());
                assignmentService.updateAssignment(assignmentFromDB);
            }

            List<AssignmentDto> assignmentDtos = assignmentService.findAssignmentDtoBySubjectIdAndClassId(assignmentFromDB.getSubjectId(), assignmentFromDB.getClassId());

            model.addAttribute("subjectName", subject.getName());
            model.addAttribute("assignments", assignmentDtos);
            model.addAttribute("message", "Sukses Mengunggah Jawaban");

            String userRole = GlobalFunction.getUserRole(request);

            Student student = studentService.findByUserId(userId);
            List<StudentAsgDto> studentAsgDtos = assignmentService.findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(assignmentFromDB.getSubjectId(), student.getClassId(), student.getId());
            Class cl = classService.findById(student.getClassId());

            model.addAttribute("assignments", studentAsgDtos);
            model.addAttribute("className", cl.getName());
            model.addAttribute("studentId", student.getId());

            return "manage-assignment-parents";

        } else {
            Assignment assignment = assignmentService.findById(answerSheetRequestDto.getAssignmentId());
            Subject subject = subjectService.findById(assignment.getSubjectId());
            Class cl = classService.findById(assignment.getClassId());

            model.addAttribute("answerSheetRequestDto", answerSheetRequestDto);
            model.addAttribute("subjectName", subject.getName());
            model.addAttribute("className", cl.getName());
            model.addAttribute("deadline", assignment.getDeadline());

            return "add-answer-sheet";
        }
    }

    @GetMapping("/get/{assignmentId}/{studentId}")
    public ResponseEntity<Resource> getAnswerSheetFile(@PathVariable("assignmentId") Long assignmentId, @PathVariable("studentId") Long studentId) {
        Assignment assignmentFromDB = assignmentService.findById(assignmentId);
        AnswerSheet answerSheet = answerSheetService.findByAssignmentIdAndStudentId(assignmentId, studentId);
        ByteArrayResource resource = new ByteArrayResource(answerSheet.getFile());

        StudentDto student = studentService.findById(answerSheet.getStudentId());
        Subject subject = subjectService.findById(assignmentFromDB.getSubjectId());
        Class cl = classService.findById(assignmentFromDB.getClassId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", String.format("attachment; filename=" + student.getUser().getFullName() + "_" + subject.getName() + "_" + assignmentFromDB.getTitle() + ".pdf"));

        return ResponseEntity.ok().headers(httpHeaders).contentLength(answerSheet.getFile().length).contentType(MediaType.parseMediaType("application/cbor")).body(resource);
    }

}
