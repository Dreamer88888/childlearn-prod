package com.childlearn.controller;


import com.childlearn.dto.ClassDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.service.ClassService;
import com.childlearn.service.TeacherService;
import com.childlearn.service.UserService;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewClass(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("cls", classService.findAll());

        return "manage-class";
    }

    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        classService.deleteClass(id);

        model.addAttribute("cls", classService.findAll());
        model.addAttribute("message", "Sukses Menghapus Kelas");

        return "manage-class";
    }

    @GetMapping("/update/{id}")
    public String getViewUpdateClass(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Class cl = classService.findById(id);
        if (cl != null) {
            model.addAttribute("cl", cl);
            model.addAttribute("teachers", teacherService.findAllTeacherDto());
        }

        return "edit-class";
    }

    @PostMapping("/update")
    public String updateClass(@Valid @ModelAttribute("cl") Class cl, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            classService.updateClass(cl);

            model.addAttribute("cls", classService.findAll());
            model.addAttribute("message", "Sukses Menyunting Kelas");

            return "manage-class";
        } else {
            model.addAttribute("cl", cl);
            model.addAttribute("teachers", teacherService.findAllTeacherDto());

            return "edit-class";
        }
    }

    @GetMapping("/add")
    public String getViewAddClass(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("classDto", new ClassDto());
        model.addAttribute("teachers", teacherService.findAllTeacherDto());

        return "add-class";
    }

    @PostMapping("/add")
    public String addClass(@Valid @ModelAttribute("classDto") ClassDto classDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("teachers", teacherService.findAllTeacherDto());

        if (!bindingResult.hasErrors()) {
            classService.createClass(new Class(classDto.getName(), classDto.getTeacherId()));

            model.addAttribute("classDto", new ClassDto());
            model.addAttribute("message", "Sukses Menambahkan Kelas");

            return "add-class";
        } else {
            model.addAttribute("classDto", classDto);

            return "add-class";
        }
    }

}
