package com.childlearn.controller;


import com.childlearn.dto.SubjectDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Subject;
import com.childlearn.service.ClassService;
import com.childlearn.service.SubjectService;
import com.childlearn.service.UserService;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService service;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewSubject(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("subjects", service.findAll());

        return "manage-subject";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeleteSubjectSuccess = service.deleteSubject(id);

        if (!isDeleteSubjectSuccess) {
            log.info("Fail delete subject with id " + id);
        }

        model.addAttribute("subjects", service.findAll());
        model.addAttribute("message", "Sukses Menghapus Mata Pelajaran");

        return "manage-subject";
    }

    @GetMapping("/update/{id}")
    public String getViewUpdateSubject(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Subject subject = service.findById(id);
        if (subject != null) {
            model.addAttribute("subject", service.findById(id));
        }

        return "edit-subject";
    }

    @PostMapping("/update")
    public String updateSubject(@Valid @ModelAttribute("subject") Subject subject, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            service.updateSubject(subject);

            model.addAttribute("subjects", service.findAll());
            model.addAttribute("message", "Sukses Menyunting Mata Pelajaran");

            return "manage-subject";
        } else {
            model.addAttribute("subject", subject);

            return "edit-subject";
        }
    }

    @GetMapping("/add")
    public String getViewAddSubject(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("subjectDto", new SubjectDto());

        return "add-subject";
    }

    @PostMapping("/add")
    public String addSubject(@Valid @ModelAttribute("subjectDto") SubjectDto subjectDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            Subject subject = new Subject();
            subject.setName(subjectDto.getName());

            service.createSubject(subject);

            model.addAttribute("subjectDto", new SubjectDto());
            model.addAttribute("message", "Sukses Menambahkan Mata Pelajaran");

            return "add-subject";
        } else {
            model.addAttribute("subjectDto", subjectDto);

            return "add-subject";
        }
    }

}
