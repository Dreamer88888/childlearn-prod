package com.childlearn.service;

import com.childlearn.dto.AgendaDisplayDto;
import com.childlearn.dto.AgendaHeaderRequestDto;
import com.childlearn.entity.AgendaDetail;
import com.childlearn.entity.AgendaHeader;
import com.childlearn.entity.Subject;
import com.childlearn.repository.AgendaHeaderRepository;
import com.childlearn.util.GlobalFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgendaHeaderService {

    @Autowired
    private AgendaHeaderRepository agendaHeaderRepository;

    @Autowired
    private AgendaDetailService agendaDetailService;

    @Autowired
    private SubjectService subjectService;

    public List<AgendaHeader> findAll() {
        return agendaHeaderRepository.findAll();
    }

    public List<AgendaDisplayDto> findAllDtoByClassId(Long classId) {
        List<AgendaDisplayDto> agendaDisplayDtos = findByClassId(classId).stream().map(agendaHeader -> {
            AgendaDisplayDto agendaDisplayDto = new AgendaDisplayDto();

            agendaDisplayDto.setDate(GlobalFunction.convertLocaleIndonesiaDate(agendaHeader.getDate()));

            List<AgendaDetail> agendaDetails = agendaDetailService.findByAgendaHeaderId(agendaHeader.getId());

            boolean isFirst = true;
            List<String> subjectNameToAdd = new ArrayList<>();
            List<String> titleToAdd = new ArrayList<>();

            for (AgendaDetail agendaDetail : agendaDetails) {
                Subject subject = subjectService.findById(agendaDetail.getSubjectId());
                if (isFirst) {
                    agendaDisplayDto.setCount(agendaDetails.size());
                    isFirst = !isFirst;
                }
                subjectNameToAdd.add(subject.getName());
                titleToAdd.add(agendaDetail.getTitle());
            }

            agendaDisplayDto.setSubjectNames(subjectNameToAdd);
            agendaDisplayDto.setTitles(titleToAdd);
            agendaDisplayDto.setId(agendaHeader.getId());

            return agendaDisplayDto;

        }).collect(Collectors.toList());

        return agendaDisplayDtos;
    }

    public AgendaHeader findById(Long id) {
        Optional<AgendaHeader> agendaHeader = agendaHeaderRepository.findById(id);

        if (agendaHeader.isPresent()) {
            return agendaHeader.get();
        } else {
            return null;
        }
    }

    public List<AgendaHeader> findByClassId(Long classId) {
        Optional<List<AgendaHeader>> agendaHeaders = agendaHeaderRepository.findByClassId(classId);
        if (agendaHeaders.isPresent()) {
            return agendaHeaders.get();
        } else {
            throw new NotFoundException("Agenda Header with classId " + classId.toString() + " not found.");
        }
    }

    public AgendaHeader findByClassIdAndCheckDate(Long classId, Date date) {
        Optional<List<AgendaHeader>> agendaHeader = agendaHeaderRepository.findByClassId(classId);
        if (agendaHeader.isPresent()) {
            List<AgendaHeader> agendaHeaders = agendaHeader.get();

            for (AgendaHeader agendaHeaderLoop : agendaHeaders) {
                if (agendaHeaderLoop.getDate().compareTo(date) == 0) {
                    return agendaHeaderLoop;
                }
            }
            return null;
        } else {
            throw new NotFoundException("Agenda Header with ClassId " + classId.toString() + " and Date " + date.toString() + " not found.");
        }
    }

    public String getAgendasDate(Long classId) {
        List<AgendaHeader> agendaHeaders = findByClassId(classId);
        String agendasDate = "";

        for (int i = 0; i < agendaHeaders.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
            String formattedDate = sdf.format(agendaHeaders.get(i).getDate());
            agendasDate = agendasDate + formattedDate;

            if (i < agendaHeaders.size() - 1) {
                agendasDate = agendasDate + "#";
            }
        }

        return agendasDate;
    }

    public AgendaHeader createAgendaHeader(AgendaHeaderRequestDto agendaHeaderRequestDto) {
        AgendaHeader agendaHeader = new AgendaHeader();
        agendaHeader.setDate(agendaHeaderRequestDto.getDate());
        agendaHeader.setClassId(agendaHeaderRequestDto.getClassId());

        return agendaHeaderRepository.save(agendaHeader);
    }

    public AgendaHeader updateAgendaHeader(AgendaHeader agendaHeader) {
        Long agendaHeaderId = agendaHeader.getId();
        Optional<AgendaHeader> updatedAgendaHeader = agendaHeaderRepository.findById(agendaHeaderId);
        if (updatedAgendaHeader.isPresent()) {
            return agendaHeaderRepository.save(agendaHeader);
        } else {
            return null;
        }
    }

    public boolean deleteAgendaHeader(Long id) {
        Optional<AgendaHeader> deletedAgendaHeader = agendaHeaderRepository.findById(id);
        if (deletedAgendaHeader.isPresent()) {
            agendaHeaderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
