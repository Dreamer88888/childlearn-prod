package com.childlearn.controller;

import com.childlearn.dto.*;
import com.childlearn.entity.*;
import com.childlearn.entity.Class;
import com.childlearn.service.*;
import com.childlearn.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaHeaderService agendaHeaderService;

    @Autowired
    private AgendaDetailService agendaDetailService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public String getViewAgendaForParent(@PathVariable(name = "userId") Long pathUserId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Student student = studentService.findByUserId(pathUserId);

        Class cl = classService.findById(student.getClassId());
        List<AgendaDisplayDto> agendaDisplayDtos = agendaHeaderService.findAllDtoByClassId(student.getClassId());
        String agendasDate = agendaHeaderService.getAgendasDate(cl.getId());

        model.addAttribute("agendaDisplayDtos", agendaDisplayDtos);
        model.addAttribute("className", cl.getName());
        model.addAttribute("classId", student.getClassId());
        model.addAttribute("agendasDate", agendasDate);

        return "manage-agenda";
    }

    @GetMapping
    public String getViewManageAgenda(@RequestParam Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        Class cl = classService.findById(classId);
        List<AgendaDisplayDto> agendaDisplayDtos = agendaHeaderService.findAllDtoByClassId(classId);
        String agendasDate = agendaHeaderService.getAgendasDate(classId);

        model.addAttribute("agendaDisplayDtos", agendaDisplayDtos);
        model.addAttribute("className", cl.getName());
        model.addAttribute("classId", classId);
        model.addAttribute("agendasDate", agendasDate);

        return "manage-agenda";
    }

    @GetMapping("/add")
    public String getViewAddAgendaByClassId(@RequestParam Long classId, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        AgendaRequestDto agendaRequestDto = new AgendaRequestDto();
        agendaRequestDto.setClassId(classId);
        String agendasDate = agendaHeaderService.getAgendasDate(classId);

        model.addAttribute("agendaRequestDto", agendaRequestDto);
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cls", cls);
        model.addAttribute("agendasDate", agendasDate);

        return "add-agenda";
    }

    @PostMapping("/add")
    public String addAgendaByClassId(@Valid @ModelAttribute("agendaRequestDto") AgendaRequestDto agendaRequestDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));

        List<Subject> subjects = subjectService.findAll();

        if (!bindingResult.hasErrors()) {
            AgendaHeader agendaHeader = agendaHeaderService.findByClassIdAndCheckDate(agendaRequestDto.getClassId(), agendaRequestDto.getDate());

            if (agendaHeader == null) {
                AgendaHeaderRequestDto agendaHeaderRequestDto = new AgendaHeaderRequestDto();
                agendaHeaderRequestDto.setDate(agendaRequestDto.getDate());
                agendaHeaderRequestDto.setClassId(agendaRequestDto.getClassId());

                agendaHeader = agendaHeaderService.createAgendaHeader(agendaHeaderRequestDto);
            }

            AgendaDetailRequestDto agendaDetailRequestDto = new AgendaDetailRequestDto();
            agendaDetailRequestDto.setAgendaHeaderId(agendaHeader.getId());
            agendaDetailRequestDto.setSubjectId(agendaRequestDto.getSubjectId());
            agendaDetailRequestDto.setTitle(agendaRequestDto.getTitle());

            AgendaDetail agendaDetail = agendaDetailService.createAgendaDetail(agendaDetailRequestDto);

            AgendaRequestDto newAgendaRequestDto = new AgendaRequestDto();
            newAgendaRequestDto.setClassId(agendaRequestDto.getClassId());
            String agendasDate = agendaHeaderService.getAgendasDate(agendaRequestDto.getClassId());

            model.addAttribute("agendaRequestDto", newAgendaRequestDto);
            model.addAttribute("subjects", subjectService.findAll());
            model.addAttribute("cls", cls);
            model.addAttribute("agendasDate", agendasDate);
            model.addAttribute("message", "Sukses Menambahkan Agenda");

            return "add-agenda";
        } else {
            Class cl = classService.findById(agendaRequestDto.getClassId());
            String agendasDate = agendaHeaderService.getAgendasDate(agendaRequestDto.getClassId());

            model.addAttribute("agendaRequestDto", agendaRequestDto);
            model.addAttribute("className", cl.getName());
            model.addAttribute("subjects", subjects);
            model.addAttribute("agendasDate", agendasDate);
            model.addAttribute("cls", cls);

            return "add-agenda";
        }

    }

    @GetMapping("/delete/{agendaHeaderId}/{classId}")
    public String deleteAgendaByAgendaHeaderId(@PathVariable(name = "agendaHeaderId") Long agendaHeaderId, @PathVariable(name = "classId") Long classId,
                                               HttpServletRequest request, Model model) {
        String role = GlobalFunction.getUserRole(request);
        Long userId = Long.valueOf(GlobalFunction.getUserId(request));
        String b64 = userService.findBase64ByUserId(userId);
        String fullName = GlobalFunction.getUserFullName(request);
        List<Class> cls = classService.findAll();

        model.addAttribute("userDetail", new UserDetailDto(role, fullName, cls, b64, userId));
        boolean isDeleteAgendaDetailSuccess = false;
        boolean isDeleteAgendaHeaderSuccess = false;

        List<AgendaDetail> agendaDetails = agendaDetailService.findByAgendaHeaderId(agendaHeaderId);
        for (AgendaDetail agendaDetail : agendaDetails) {
            isDeleteAgendaDetailSuccess = agendaDetailService.deleteAgendaDetail(agendaDetail.getId());
            if (!isDeleteAgendaDetailSuccess){
                log.error("Fail delete Agenda Detail with id " + agendaDetail.getId());
            }
        }

        isDeleteAgendaHeaderSuccess = agendaHeaderService.deleteAgendaHeader(agendaHeaderId);
        if (!isDeleteAgendaHeaderSuccess) {
            log.error("Fail delete Agenda Header with id " + agendaHeaderId);
        }

        Class cl = classService.findById(classId);
        List<AgendaDisplayDto> agendaDisplayDtos = agendaHeaderService.findAllDtoByClassId(classId);
        String agendasDate = agendaHeaderService.getAgendasDate(classId);

        model.addAttribute("agendaDisplayDtos", agendaDisplayDtos);
        model.addAttribute("className", cl.getName());
        model.addAttribute("classId", classId);
        model.addAttribute("agendasDate", agendasDate);

        if(isDeleteAgendaDetailSuccess && isDeleteAgendaHeaderSuccess) {
            model.addAttribute("message", "Sukses Menghapus Agenda");
        }

        return "manage-agenda";
    }

}
