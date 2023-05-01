package com.childlearn.controller;

import com.childlearn.dto.NewsletterRequestDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.Class;
import com.childlearn.service.ClassService;
import com.childlearn.service.NewsletterService;
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
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String getViewAddNewsletter(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("newsletterRequestDto", new NewsletterRequestDto());

        return "add-newsletter";
    }

    @PostMapping("/add")
    public String createNewsletter(@Valid @ModelAttribute("newsletterRequestDto") NewsletterRequestDto newsletterRequestDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        if (!bindingResult.hasErrors()) {
            try {
                newsletterRequestDto.setTeacherId(userId);
                newsletterService.createNewsletter(newsletterRequestDto);

                model.addAttribute("newsletterRequestDto", new NewsletterRequestDto());
                model.addAttribute("message", "Sukses Menambahkan Berita");

                return "add-newsletter";
            } catch (Exception e) {
                model.addAttribute("newsletterRequestDto", newsletterRequestDto);

                return "add-newsletter";
            }
        } else {
            model.addAttribute("newsletterRequestDto", newsletterRequestDto);

            return "add-newsletter";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteNewsletter(@PathVariable Long id, HttpServletRequest request, Model model, RedirectAttributes redirectAttrs) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Boolean isDeleteSuccess = newsletterService.deleteNewsletter(id);

        model.addAttribute("newsletters", newsletterService.findAll());
        if (isDeleteSuccess) {
            redirectAttrs.addFlashAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
            redirectAttrs.addFlashAttribute("message", "Sukses Menghapus Berita");
            return "redirect:/";
        } else {
            log.error("Error delete berita");

            return "redirect:/";
        }
    }

}
