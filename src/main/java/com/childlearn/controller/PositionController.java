package com.childlearn.controller;

import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Position;
import com.childlearn.service.ClassService;
import com.childlearn.service.PositionService;
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
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService service;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewPosition(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("positions", service.findAll());

        return "manage-position";
    }

    @GetMapping("/add")
    public String getViewAddPosition(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("position", new Position());

        return "add-position";
    }

    @PostMapping("/add")
    public String addPosition(@Valid @ModelAttribute("position") Position position, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            service.createPosition(position);

            model.addAttribute("position", new Position());
            model.addAttribute("message", "Sukses Menambahkan Posisi Guru");

            return "add-position";
        } else {
            model.addAttribute("position", position);

            return "add-position";
        }

    }

    @GetMapping("/update/{id}")
    public String getViewUpdatePosition(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Position updatedPosition = service.findById(id);

        if (updatedPosition != null) {
            model.addAttribute("position", updatedPosition);

            return "edit-position";
        } else {
            model.addAttribute("positions", service.findAll());

            return "manage-position";
        }
    }

    @PostMapping("/update")
    public String updatePosition(@Valid @ModelAttribute("position") Position position, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            service.updatePosition(position);

            model.addAttribute("positions", service.findAll());
            model.addAttribute("message", "Sukses Menyunting Posisi Guru");

            return "manage-position";
        } else {
            model.addAttribute("position", position);

            return "edit-position";
        }

    }

    @GetMapping("/delete/{id}")
    public String deletePosition(@PathVariable Long id, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeletePositionSuccess = service.deletePosition(id);

        if (isDeletePositionSuccess) {
            model.addAttribute("positions", service.findAll());
            model.addAttribute("message", "Sukses Menghapus Posisi Guru");

            return "manage-position";
        } else {
            model.addAttribute("positions", service.findAll());
            log.error("Error delete position");
            
            return "manage-position";
        }
    }

}
