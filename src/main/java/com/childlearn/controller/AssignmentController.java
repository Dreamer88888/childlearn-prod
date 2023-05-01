package com.childlearn.controller;

import com.childlearn.dto.*;
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
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassService classService;

    @Autowired
    private AnswerSheetService answerSheetService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String getViewAllAssignmentForParent(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();

        Student student = studentService.findByUserId(userId);

        model.addAttribute("subjects", subjects);
        model.addAttribute("classId", student.getClassId());
        model.addAttribute("selectedClass", classService.findNameById(student.getClassId()));

        return "list-assignment";
    }

    @GetMapping("/{classId}")
    public String getViewAllAssignmentForTeacher(@PathVariable Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();

        model.addAttribute("subjects", subjects);
        model.addAttribute("classId", classId);
        model.addAttribute("selectedClass", classService.findNameById(classId));

        return "list-assignment";
    }

    @GetMapping("/{subjectId}/{classId}")
    public String getViewAssignmentBySubjectId(@PathVariable(name = "subjectId") Long subjectId, @PathVariable(name = "classId") Long classId, Model model, HttpServletRequest request) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Subject subject = subjectService.findById(subjectId);

        model.addAttribute("subjectName", subject.getName());
        model.addAttribute("classId", classId);
        model.addAttribute("subjectId", subjectId);

        String userRole = GlobalFunction.getUserRole(request);

        if (userRole.equals("TEACHER")) {
            List<AssignmentDto> assignmentDtos = assignmentService.findAssignmentDtoBySubjectIdAndClassId(subjectId, classId);
            log.info(assignmentDtos.toString());

            Class cl = classService.findById(classId);
            List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

            model.addAttribute("assignments", assignmentDtos);
            model.addAttribute("className", cl.getName());
            model.addAttribute("students", studentDtos);
            model.addAttribute("scoreDto", new ScoreDto(-1, null));

            return "manage-assignment-teachers";
        } else {
            Student student = studentService.findByUserId(userId);
            List<StudentAsgDto> studentAsgDtos = assignmentService.findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(subjectId, classId, student.getId());
            Class cl = classService.findById(student.getClassId());

            model.addAttribute("assignments", studentAsgDtos);
            model.addAttribute("className", cl.getName());
            model.addAttribute("studentId", student.getId());

            return "manage-assignment-parents";
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Resource> getAssignmentFile(@PathVariable Long id) {
        Assignment assignment = assignmentService.findById(id);
        ByteArrayResource resource = new ByteArrayResource(assignment.getFile());

        Class cl = classService.findById(assignment.getClassId());
        Subject subject = subjectService.findById(assignment.getSubjectId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", String.format("attachment; filename=" + cl.getName() + "_" + subject.getName() + "_" + assignment.getTitle() + ".pdf"));

        return ResponseEntity.ok().headers(httpHeaders).contentLength(assignment.getFile().length).contentType(MediaType.parseMediaType("application/cbor")).body(resource);
    }

    @GetMapping("/delete/{assignmentId}/{subjectId}/{classId}")
    public String deleteAssignment(@PathVariable(name = "assignmentId") Long assignmentId, @PathVariable(name = "subjectId") Long subjectId,
                                   @PathVariable(name = "classId") Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<AnswerSheet> answerSheets = answerSheetService.findByAssignmentId(assignmentId);
        
        boolean isDeleteAnswerSheetSuccess = false;
        boolean isDeleteAssignmentSuccess = false;

        for (AnswerSheet answerSheet : answerSheets) {
            isDeleteAnswerSheetSuccess = answerSheetService.deleteAnswerSheet(answerSheet.getId());
            if (!isDeleteAnswerSheetSuccess) {
                log.error("Fail delete Answer Sheet with id " + answerSheet.getId());
            }
        }

        isDeleteAssignmentSuccess = assignmentService.deleteAssignment(assignmentId);

        if (!isDeleteAssignmentSuccess) {
            log.error("Fail delete Assignment with id " + assignmentId);
        }

        Subject subject = subjectService.findById(subjectId);

        List<AssignmentDto> assignmentDtos = assignmentService.findAssignmentDtoBySubjectIdAndClassId(subjectId, classId);

        model.addAttribute("subjectName", subject.getName());
        model.addAttribute("assignments", assignmentDtos);
        model.addAttribute("classId", classId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("scoreDto", new ScoreDto(-1, null));

        if(isDeleteAnswerSheetSuccess && isDeleteAssignmentSuccess) {
            model.addAttribute("message", "Sukses Menghapus Tugas");
        }

        String userRole = GlobalFunction.getUserRole(request);

        if (userRole.equals("TEACHER")) {
            log.info(assignmentDtos.toString());

            Class cl = classService.findById(classId);
            List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

            model.addAttribute("assignments", assignmentDtos);
            model.addAttribute("className", cl.getName());
            model.addAttribute("students", studentDtos);
            model.addAttribute("scoreDto", new ScoreDto(-1, null));

            return "manage-assignment-teachers";
        } else {
            Student student = studentService.findByUserId(userId);
            List<StudentAsgDto> studentAsgDtos = assignmentService.findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(subjectId, classId, student.getId());
            Class cl = classService.findById(student.getClassId());

            model.addAttribute("assignments", studentAsgDtos);
            model.addAttribute("className", cl.getName());
            model.addAttribute("studentId", student.getId());

            return "manage-assignment-parents";
        }
    }

    @PostMapping("/score/{answerSheetId}")
    public String addScore(@PathVariable(name = "answerSheetId") Long answerSheetId,
                           @Valid @ModelAttribute("scoreDto") ScoreDto scoreDto, BindingResult bindingResult,
                           HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        AnswerSheet answerSheetFromDB = answerSheetService.findById(answerSheetId);
        Assignment assignmentFromDB = assignmentService.findById(answerSheetFromDB.getAssignmentId());

        log.info("LOG JOE: " + scoreDto.getScore());

        if (!bindingResult.hasErrors()) {

            if (answerSheetFromDB != null) {
                answerSheetFromDB.setScore(scoreDto.getScore());
                answerSheetService.updateAnswerSheet(answerSheetFromDB);

                Long subjectId = assignmentFromDB.getSubjectId();
                Long classId = assignmentFromDB.getClassId();
                Subject subject = subjectService.findById(subjectId);

                model.addAttribute("subjectName", subject.getName());
                model.addAttribute("classId", classId);
                model.addAttribute("subjectId", subjectId);

                if (role.equals("TEACHER")) {
                    List<AssignmentDto> assignmentDtos = assignmentService.findAssignmentDtoBySubjectIdAndClassId(subjectId, classId);
                    log.info(assignmentDtos.toString());

                    Class cl = classService.findById(classId);
                    List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

                    model.addAttribute("assignments", assignmentDtos);
                    model.addAttribute("className", cl.getName());
                    model.addAttribute("students", studentDtos);
                    model.addAttribute("scoreDto", new ScoreDto(-1, null));

                    model.addAttribute("message", "Sukses Memberikan Nilai");

                    return "manage-assignment-teachers";
                } else {
                    Student student = studentService.findByUserId(userId);
                    List<StudentAsgDto> studentAsgDtos = assignmentService.findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(subjectId, classId, student.getId());
                    Class cl = classService.findById(student.getClassId());

                    model.addAttribute("assignments", studentAsgDtos);
                    model.addAttribute("className", cl.getName());
                    model.addAttribute("studentId", student.getId());

                    return "manage-assignment-parents";
                }
            } else {
                throw new NotFoundException("Answer Sheet with id " + answerSheetId + " not found");
            }
        } else {
            Long subjectId = assignmentFromDB.getSubjectId();
            Long classId = assignmentFromDB.getClassId();
            Subject subject = subjectService.findById(subjectId);
            List<AssignmentDto> assignmentDtos = assignmentService.findAssignmentDtoBySubjectIdAndClassId(subjectId, classId);

            model.addAttribute("subjectName", subject.getName());
            model.addAttribute("assignments", assignmentDtos);
            model.addAttribute("classId", classId);
            model.addAttribute("subjectId", subjectId);

            if (role.equals("TEACHER")) {
                Class cl = classService.findById(classId);
                List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

                model.addAttribute("assignments", assignmentDtos);
                model.addAttribute("className", cl.getName());
                model.addAttribute("students", studentDtos);
                model.addAttribute("scoreDto", scoreDto);
                model.addAttribute("scoreIsEmpty", "Y");

                return "manage-assignment-teachers";
            } else {
                Student student = studentService.findByUserId(userId);
                List<StudentAsgDto> studentAsgDtos = assignmentService.findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(subjectId, classId, student.getId());
                Class cl = classService.findById(student.getClassId());

                model.addAttribute("assignments", studentAsgDtos);
                model.addAttribute("className", cl.getName());
                model.addAttribute("studentId", student.getId());

                return "manage-assignment-parents";
            }
        }
    }

    @GetMapping("/add")
    public String getViewAddAssignment(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("assignmentRequestDto", new AssignmentRequestDto());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cls", classService.findAll());

        return "add-assignment";
    }

    @PostMapping("/add")
    public String addAssignment(@Valid @ModelAttribute("assignmentRequestDto") AssignmentRequestDto assignmentRequestDto, BindingResult bindingResult, HttpServletRequest request, Model model) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();

        if (!bindingResult.hasErrors()) {
            Subject subject = subjectService.findById(assignmentRequestDto.getSubjectId());
            Class cl = classService.findById(assignmentRequestDto.getClassId());

            if (subject != null && cl != null) {
                Assignment assignment = new Assignment();

                assignment.setTitle(assignmentRequestDto.getTitle());
                assignment.setSubjectId(assignmentRequestDto.getSubjectId());
                assignment.setClassId(assignmentRequestDto.getClassId());
                assignment.setDeadline(assignmentRequestDto.getDeadline());
                assignment.setFile(assignmentRequestDto.getFile().getBytes());

                TeacherDto teacherDto = teacherService.findByUserId(userId);

                assignment.setTeacherId(teacherDto.getId());

                assignmentService.createAssignment(assignment);
            }

            model.addAttribute("assignmentRequestDto", new AssignmentRequestDto());
            model.addAttribute("subjects", subjects);
            model.addAttribute("cls", cls);

            model.addAttribute("message", "Sukses Menambahkan Tugas");

            return "add-assignment";
        } else {
            model.addAttribute("assignmentRequestDto", assignmentRequestDto);
            model.addAttribute("subjects", subjects);
            model.addAttribute("cls", cls);

            return "add-assignment";
        }
    }

}
