package com.childlearn.controller;


import com.childlearn.dto.TeacherDto;
import com.childlearn.dto.TeacherRequestDto;
import com.childlearn.dto.TeacherUpdateDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Position;
import com.childlearn.entity.Teacher;
import com.childlearn.entity.User;
import com.childlearn.service.ClassService;
import com.childlearn.service.PositionService;
import com.childlearn.service.TeacherService;
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
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private ClassService classService;

    @GetMapping
    public String getViewManageTeacher(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<TeacherDto> teacherDtos = teacherService.findAll().stream().map(teacherDto -> {
            User user = userService.findById(teacherDto.getUserId());
            Position position = positionService.findById(teacherDto.getPositionId());

            TeacherDto newTeacherDto = new TeacherDto(teacherDto.getId(), user, position);

            return newTeacherDto;
        }).collect(Collectors.toList());

        model.addAttribute("teachers", teacherDtos);

        return "manage-teacher";
    }

    @GetMapping("/add")
    public String getViewAddTeacher(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("teacherRequestDto", new TeacherRequestDto());
        model.addAttribute("users", userService.findByRoleUser());
        model.addAttribute("positions", positionService.findAll());

        return "add-teacher";
    }

    @PostMapping("/add")
    public String addTeacher(@Valid @ModelAttribute("teacherRequestDto") TeacherRequestDto teacherRequestDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            Teacher teacher = new Teacher();

            teacher.setUserId(teacherRequestDto.getUserId());
            teacher.setPositionId(teacherRequestDto.getPositionId());

            teacherService.createTeacher(teacher);

            model.addAttribute("teacherRequestDto", new TeacherRequestDto());
            model.addAttribute("users", userService.findByRoleUser());
            model.addAttribute("positions", positionService.findAll());

            model.addAttribute("message", "Sukses Menambahkan Guru");

            return "add-teacher";
        } else {
            model.addAttribute("teacherRequestDto", teacherRequestDto);
            model.addAttribute("users", userService.findByRoleUser());
            model.addAttribute("positions", positionService.findAll());

            return "add-teacher";
        }


    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeleteTeacherSuccess = teacherService.deleteTeacher(id);

        if (!isDeleteTeacherSuccess) {
            log.info("Fail delete teacher with id " + id);
        }

        List<TeacherDto> teacherDtos = teacherService.findAllTeacherDto();

        model.addAttribute("teachers", teacherDtos);
        model.addAttribute("message", "Sukses Menghapus Guru");

        return "manage-teacher";
    }

    @GetMapping("/update/{id}")
    public String getViewUpdateStudent(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        TeacherDto teacherDto = teacherService.findById(id);
        User user = userService.findById(teacherDto.getUser().getId());

        TeacherUpdateDto teacherUpdateDto = new TeacherUpdateDto(id, user.getId(), teacherDto.getPosition().getId(), user.getFullName());

        model.addAttribute("teacherUpdateDto", teacherUpdateDto);
        model.addAttribute("positions", positionService.findAll());

        return "edit-teacher";
    }

    @PostMapping("/update")
    public String updateStudent(@Valid @ModelAttribute("teacherUpdateDto") TeacherUpdateDto teacherUpdateDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            if (teacherService.updateTeacher(teacherUpdateDto) != null) {
                List<TeacherDto> teacherDtos = teacherService.findAll().stream().map(teacherDto -> {
                    User user = userService.findById(teacherDto.getUserId());
                    Position position = positionService.findById(teacherDto.getPositionId());

                    TeacherDto newTeacherDto = new TeacherDto(teacherDto.getId(), user, position);

                    return newTeacherDto;
                }).collect(Collectors.toList());

                model.addAttribute("teachers", teacherDtos);
                model.addAttribute("message", "Sukses Menyunting Guru");

                return "manage-teacher";
            } else {
                List<TeacherDto> teacherDtos = teacherService.findAll().stream().map(teacherDto -> {
                    User user = userService.findById(teacherDto.getUserId());
                    Position position = positionService.findById(teacherDto.getPositionId());

                    TeacherDto newTeacherDto = new TeacherDto(teacherDto.getId(), user, position);

                    return newTeacherDto;
                }).collect(Collectors.toList());

                log.error("Fail update teacher");

                model.addAttribute("teachers", teacherDtos);

                return "manage-teacher";
            }
        } else {
            model.addAttribute("teacherUpdateDto", teacherUpdateDto);
            model.addAttribute("positions", positionService.findAll());

            return "edit-teacher";
        }
    }

}
