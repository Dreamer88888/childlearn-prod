package com.childlearn.controller;


import com.childlearn.dto.MaterialDto;
import com.childlearn.dto.MaterialRequestDto;
import com.childlearn.dto.StudentDto;
import com.childlearn.dto.UserDetailDto;
import com.childlearn.entity.*;
import com.childlearn.entity.Class;
import com.childlearn.service.*;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getViewAssignment(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);

        Student student = studentService.findByUserId(userId);
        log.info(student.toString());

        model.addAttribute("classId", student.getClassId());
        model.addAttribute("selectedClass", classService.findNameById(student.getClassId()));

        return "list-material";
    }

    @GetMapping("/{classId}")
    public String getViewAssignmentByClassId(@PathVariable(name = "classId") Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();

        model.addAttribute("subjects", subjects);
        model.addAttribute("classId", classId);
        model.addAttribute("selectedClass", classService.findNameById(classId));

        return "list-material";
    }

    @GetMapping("/{classId}/{subjectId}")
    public String getViewMaterialBySubjectId(@PathVariable(name = "classId") Long classId, @PathVariable(name = "subjectId") Long subjectId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Subject subject = subjectService.findById(subjectId);
        Class cl = classService.findById(classId);

        model.addAttribute("subjectName", subject.getName());
        model.addAttribute("className", cl.getName());
        model.addAttribute("materials", materialService.findMaterialByClassIdAndSubjectId(classId, subjectId));

        return "manage-material";
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Resource> getMaterialFile(@PathVariable Long id) throws IOException {
        MaterialDto material = materialService.findMaterialById(id);
        ByteArrayResource resource = new ByteArrayResource(material.getFile());

        Class cl = material.getCl();
        Subject subject = material.getSubject();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", String.format("attachment; filename=" + cl.getName() + "_" + subject.getName() + "_" + material.getTitle() + ".pdf"));

        return ResponseEntity.ok().headers(httpHeaders).contentLength(material.getFile().length).contentType(MediaType.parseMediaType("application/cbor")).body(resource);
    }

    @GetMapping("/delete")
    public String deleteMaterial(@RequestParam("materialId") Long materialId, @RequestParam("subjectId") Long subjectId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        MaterialDto material = materialService.findMaterialById(materialId);
        materialService.deleteMaterial(materialId);

        Subject subject = subjectService.findById(subjectId);
        Class cl = classService.findById(material.getCl().getId());

        model.addAttribute("subjectName", subject.getName());

        model.addAttribute("className", cl.getName());
        model.addAttribute("materials", materialService.findMaterialByClassIdAndSubjectId(material.getCl().getId(), subjectId));
        model.addAttribute("message", "Sukses Menghapus Materi Pembelajaran");

        return "manage-material";
    }

    @GetMapping("/add")
    public String getViewAddMaterial(HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        model.addAttribute("material", new MaterialRequestDto());
        model.addAttribute("cls", classService.findAll());
        model.addAttribute("subjects", subjectService.findAll());

        return "add-material";
    }

    @PostMapping("/add")
    public String addMaterial(@Valid @ModelAttribute("material") MaterialRequestDto material, BindingResult bindingResult, HttpServletRequest request, Model model) throws IOException {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        if (!bindingResult.hasErrors()){
            Class cl = classService.findById(material.getClassId());
            Subject subject = subjectService.findById(material.getSubjectId());

            if (cl != null & subject != null) {
                MaterialDto materialDto = new MaterialDto();

                materialDto.setCl(cl);
                materialDto.setSubject(subject);
                materialDto.setTitle(material.getTitle());
                materialDto.setFile(material.getFile().getBytes());

                materialService.createMaterial(materialDto);
            }

            model.addAttribute("material", new MaterialRequestDto());
            model.addAttribute("cls", cls);
            model.addAttribute("subjects", subjectService.findAll());

            model.addAttribute("message", "Sukses Menambahkan Materi Pembelajaran");

            return "add-material";
        } else {
            model.addAttribute("material", material);
            model.addAttribute("cls", cls);
            model.addAttribute("subjects", subjectService.findAll());

            return "add-material";
        }

    }

}
