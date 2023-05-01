package com.childlearn.controller;

import com.childlearn.dto.TeacherSuggestDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.service.ClassService;
import com.childlearn.service.TeacherSuggestService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/teacher-suggest")
public class TeacherSuggestController {

    @Autowired
    private TeacherSuggestService service;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewTeacherSuggest(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("teacherSuggests", service.findAll());

        return "teacher-suggest";
    }

    @GetMapping("/add")
    public String getViewAddTeacherSuggest(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("teacherSuggest", new TeacherSuggestDto());

        return "add-teacher-suggest";
    }

    @PostMapping("/add")
    public String addTeacherSuggest(@Valid @ModelAttribute("teacherSuggest") TeacherSuggestDto teacherSuggest, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            try {
                service.createTeacherSuggest(teacherSuggest);

                model.addAttribute("teacherSuggest", new TeacherSuggestDto());
                model.addAttribute("message", "Sukses Menambahkan Teacher Suggest");

                return "add-teacher-suggest";
            } catch (Exception e) {
                log.warn("Error upload file: " + e.getMessage());

                model.addAttribute("teacherSuggest", new TeacherSuggestDto());

                return "add-teacher-suggest";
            }
        } else {
            model.addAttribute("teacherSuggest", teacherSuggest);

            return "add-teacher-suggest";
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteTeacherSuggest(@PathVariable Long id, HttpServletRequest request, Model model, RedirectAttributes redirectAttrs) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeleteSuccess = service.deleteTeacherSuggest(id);

        if (isDeleteSuccess) {
            redirectAttrs.addFlashAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
            redirectAttrs.addFlashAttribute("message", "Sukses Menghapus Teacher Suggest");
            return "redirect:/";
        } else {
            log.error("Error delete Teacher Suggest");
            return "redirect:/";
        }

    }

}
