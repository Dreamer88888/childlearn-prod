package com.childlearn.controller;

import com.childlearn.dto.StudentDto;
import com.childlearn.dto.StudentRequestDto;
import com.childlearn.dto.StudentUpdateDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Student;
import com.childlearn.entity.User;
import com.childlearn.service.ClassService;
import com.childlearn.service.StudentService;
import com.childlearn.service.UserService;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewStudent(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<StudentDto> studentDtos = studentService.findAll().stream().map(studentDto -> {
            Class cl = classService.findById(studentDto.getClassId());
            User user = userService.findById(studentDto.getUserId());

            StudentDto newStudentDto = new StudentDto(studentDto.getId(), cl, user);

            return newStudentDto;
        }).collect(Collectors.toList());

        model.addAttribute("students", studentDtos);

        return "manage-student";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        StudentDto studentDto = studentService.findById(id);
        Boolean isDeleteStudentSuccess = studentService.deleteStudent(id);

        if (!isDeleteStudentSuccess) {
            log.info("Fail delete student with id " + id);
        }

        List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(studentDto.getCl().getId());

        model.addAttribute("students", studentDtos);
        model.addAttribute("message", "Sukses Menghapus Murid");

        return "manage-student";
    }

    @GetMapping("/update/{id}")
    public String getViewUpdateStudent(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        StudentDto studentDto = studentService.findById(id);

        StudentUpdateDto studentUpdateDto = new StudentUpdateDto(id, studentDto.getCl().getId(), studentDto.getUser().getId(), studentDto.getUser().getFullName());

        model.addAttribute("studentUpdateDto", studentUpdateDto);
        model.addAttribute("cls", cls);

        return "edit-student";
    }

    @PostMapping("/update")
    public String updateStudent(@Valid @ModelAttribute("studentUpdateDto") StudentUpdateDto student, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            studentService.updateStudent(student);

            List<StudentDto> studentDtos = studentService.findAll().stream().map(studentDto -> {
                Class cl = classService.findById(studentDto.getClassId());
                User user = userService.findById(studentDto.getUserId());

                StudentDto newStudentDto = new StudentDto(studentDto.getId(), cl, user);

                return newStudentDto;
            }).collect(Collectors.toList());

            model.addAttribute("students", studentDtos);
            model.addAttribute("message", "Sukses Menyunting Murid");

            return "manage-student";
        } else {
            model.addAttribute("studentUpdateDto", student);
            model.addAttribute("cls", classService.findAll());

            return "edit-student";
        }

    }

    @GetMapping("/add")
    public String getViewAddStudent(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("studentRequestDto", new StudentRequestDto());
        model.addAttribute("cls", classService.findAll());
        model.addAttribute("users", userService.findByRoleUser());

        return "add-student";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("studentRequestDto") StudentRequestDto studentRequestDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            Student student = new Student();

            student.setClassId(studentRequestDto.getClassId());
            student.setUserId(studentRequestDto.getUserId());

            studentService.createStudent(student);

            model.addAttribute("studentRequestDto", new StudentRequestDto());
            model.addAttribute("cls", classService.findAll());
            model.addAttribute("users", userService.findByRoleUser());
            model.addAttribute("message", "Sukses Menambahkan Murid");

            return "add-student";
        } else {
            model.addAttribute("studentRequestDto", studentRequestDto);
            model.addAttribute("cls", classService.findAll());
            model.addAttribute("users", userService.findByRoleUser());

            return "add-student";
        }

    }

}
