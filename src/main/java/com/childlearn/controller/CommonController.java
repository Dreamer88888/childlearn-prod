package com.childlearn.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.childlearn.dto.*;
import com.childlearn.entity.Class;
import com.childlearn.entity.Student;
import com.childlearn.entity.Teacher;
import com.childlearn.entity.TeacherSuggest;
import com.childlearn.service.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.childlearn.entity.User;
import com.childlearn.util.GlobalFunction;

@Controller
@Slf4j
@RequestMapping("")
public class CommonController {

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherSuggestService teacherSuggestService;

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private UserService userService;

    private static final String KEPALA_SEKOLAH = "Kepala Sekolah";
    private static final String WAKIL_KEPALA_SEKOLAH = "Wakil Kepala Sekolah";
    private static final String WALI_KELAS = "Wali Kelas";

    @GetMapping("/")
    public String homePage(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("teacherSuggests", teacherSuggestService.findAll());
        model.addAttribute("newsletters", newsletterService.findAll());
        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        return "index";
    }
    
    @GetMapping("/school-organization")
    public String navigateToSchoolOrg(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<UserDisplayDto> hmViceHm = new ArrayList<>();
        List<UserDisplayDto> teachers = new ArrayList<>();

        User headMaster = userService.findByRoleHeadMaster();
//        User viceHeadMaster = teacherService.findViceHeadMaster().getUser();

        String hmBase64 = Base64.getEncoder().encodeToString(headMaster.getFile());

        hmViceHm.add(new UserDisplayDto(headMaster.getId(), headMaster.getFullName(), hmBase64, headMaster.getPhoneNumber(), KEPALA_SEKOLAH));
//        hmViceHm.add(new User(viceHeadMaster.getId(), viceHeadMaster.getFullName(), GlobalFunction.getRandomImgPath(), viceHeadMaster.getPhoneNumber(), WAKIL_KEPALA_SEKOLAH));

        for (TeacherDto teacherDto : teacherService.findHomeRoomTeacher()) {
            String teacherBase64 = Base64.getEncoder().encodeToString(teacherDto.getUser().getFile());
            teachers.add(new UserDisplayDto(teacherDto.getUser().getId(), teacherDto.getUser().getFullName(), teacherBase64, teacherDto.getUser().getPhoneNumber(), WALI_KELAS));
        }
        for (TeacherDto teacherDto : teacherService.findGeneralTeacher()) {
            String teacherBase64 = Base64.getEncoder().encodeToString(teacherDto.getUser().getFile());
            teachers.add(new UserDisplayDto(teacherDto.getUser().getId(), teacherDto.getUser().getFullName(), teacherBase64, teacherDto.getUser().getPhoneNumber(), teacherDto.getPosition().getName()));
        }

        model.addAttribute("hms", hmViceHm);
        model.addAttribute("teachers", teachers);
        model.addAttribute("userRole", GlobalFunction.getUserRole(request));



        return "school-organization";
    }

    @GetMapping("/class-participation/{classId}")
    public String navigateToClassParticipation(@PathVariable Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Class cl = classService.findById(classId);
        TeacherDto teacherDto = teacherService.findById(cl.getTeacherId());

        List<UserDisplayDto> students = studentService.findAllStudentDtoByClassId(classId).stream().map(student -> {
            String base64 = Base64.getEncoder().encodeToString(student.getUser().getFile());

            return new UserDisplayDto(student.getUser().getFullName(), base64);
        }).collect(Collectors.toList());

        String teacherBase64 = Base64.getEncoder().encodeToString(teacherDto.getUser().getFile());

        model.addAttribute("teacher", new UserDisplayDto(teacherDto.getUser().getId(), teacherDto.getUser().getFullName(), teacherBase64, teacherDto.getUser().getPhoneNumber(), "Wali Kelas"));
        model.addAttribute("students", students);
        model.addAttribute("userRole", GlobalFunction.getUserRole(request));

        return "class-participation";
    }

    @GetMapping("/class-participation")
    public String navigateToClassParticipation(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Student studentFromDB = studentService.findByUserId(userId);
        log.info(studentFromDB.toString());

        Class cl = classService.findById(studentFromDB.getClassId());
        TeacherDto teacherDto = teacherService.findById(cl.getTeacherId());

        List<UserDisplayDto> students = studentService.findAllStudentDtoByClassId(studentFromDB.getClassId()).stream().map(student -> {
            String base64 = Base64.getEncoder().encodeToString(student.getUser().getFile());

            return new UserDisplayDto(student.getUser().getFullName(), base64);
        }).collect(Collectors.toList());

        String teacherBase64 = Base64.getEncoder().encodeToString(teacherDto.getUser().getFile());

        model.addAttribute("teacher", new UserDisplayDto(teacherDto.getUser().getId(), teacherDto.getUser().getFullName(), teacherBase64, teacherDto.getUser().getPhoneNumber(), "Wali Kelas"));
        model.addAttribute("students", students);
        model.addAttribute("userRole", GlobalFunction.getUserRole(request));


        return "class-participation";
    }

    @GetMapping("/dashboard")
    public String getViewDashboard(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("teacherSuggests", teacherSuggestService.findAll());
        model.addAttribute("newsletters", newsletterService.findAll());

        return "redirect:/";
    }

}
