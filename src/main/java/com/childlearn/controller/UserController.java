package com.childlearn.controller;

import com.childlearn.dto.UserDetailDto;
import com.childlearn.dto.UserDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.User;
import com.childlearn.service.ClassService;
import com.childlearn.service.UserService;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;


@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @GetMapping("/add")
    public String getViewCreateUser(UserDto userDto, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("userDto", new UserDto());

        return "add-user";
    }

    @PostMapping("/add")
    public String createUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, HttpServletRequest request, Model model) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            User user = new User();

            user.setFullName(userDto.getFullName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setDob(userDto.getDob());
            user.setFile(userDto.getFile().getBytes());

            userService.createUser(user);

            model.addAttribute("userDto", new UserDto());
            model.addAttribute("message", "Sukses Menambahkan Akun Pengguna");

            return "add-user";
        } else {
            model.addAttribute("userDto", userDto);

            return "add-user";
        }
    }

    

}
