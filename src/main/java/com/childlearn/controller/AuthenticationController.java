package com.childlearn.controller;

import com.childlearn.dto.CredentialDto;
import com.childlearn.dto.ManagePasswordDto;
import com.childlearn.dto.TeacherSuggestDisplayDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.User;
import com.childlearn.service.ClassService;
import com.childlearn.service.NewsletterService;
import com.childlearn.service.TeacherSuggestService;
import com.childlearn.service.UserService;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private TeacherSuggestService teacherSuggestService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        model.addAttribute("credentialDto", new CredentialDto());
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid @ModelAttribute("credentialDto") CredentialDto credentialDto, BindingResult bindingResult, Model model, HttpServletResponse response) throws UserPrincipalNotFoundException {
        if (!bindingResult.hasErrors()) {
            Optional<User> user = userService.validateCredential(credentialDto);
            Cookie cookie = new Cookie("CUSTOM_COOKIE", GlobalFunction.generateCustomCookie(user.get()));

            cookie.setMaxAge(3600);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

            List<Class> cls = classService.findAll();

            model.addAttribute("teacherSuggests", teacherSuggestService.findAll());
            model.addAttribute("newsletters", newsletterService.findAll());
            model.addAttribute("userDetail", new UserDetailDto(user.get().getRole(), user.get().getFullName(), cls));

            return "redirect:/";
        } else {
            model.addAttribute("credentialDto", credentialDto);

            return "login";
        }
    }

    @GetMapping("/logout")
    public String performLogout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Cookie[]> cookies = Optional.ofNullable(request.getCookies());

        if (cookies.isPresent()) {
            for (int i = 0; i < cookies.get().length; i++) {
                Cookie cookie = cookies.get()[i];
                cookie.setMaxAge(0);

                response.addCookie(cookie);
            }
        }
        model.addAttribute("credentialDto", new CredentialDto());

        return "login";
    }

    @GetMapping("/manage-password")
    public String getViewManagePassword(Model model, HttpServletRequest request) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("managePasswordDto", new ManagePasswordDto(userId));

        return "manage-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("managePasswordDto") ManagePasswordDto managePasswordDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()) {
            User user = userService.findById(managePasswordDto.getUserId());

            user.setPassword(managePasswordDto.getNewPassword());

            userService.updateUser(user);

            model.addAttribute("managePasswordDto", new ManagePasswordDto(managePasswordDto.getUserId()));
            model.addAttribute("message", "Sukses Mengganti Password");

            return "manage-password";
        } else {
            model.addAttribute("managePasswordDto", managePasswordDto);

            return "manage-password";
        }
    }

}
