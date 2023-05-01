package com.childlearn.service;

import com.childlearn.dto.MarkedSubjectDto;
import com.childlearn.entity.Subject;
import com.childlearn.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            return subject.get();
        } else {
            throw new NotFoundException("Subject with id " + id.toString() + " not found");
        }
    }

    public List<MarkedSubjectDto> initializeSubjectWithReportMark() {
        List<Subject> subjects = findAll();
        List<MarkedSubjectDto> markedSubjectDtos = new ArrayList<>();

        for (Subject subject : subjects) {
            markedSubjectDtos.add(new MarkedSubjectDto(subject.getId(), subject.getName(), false));
        }

        return markedSubjectDtos;
    }

    public Boolean checkIfAllSubjectIsFilled(List<MarkedSubjectDto> markedSubjectDtos) {
        int counter = 0;
        for (MarkedSubjectDto markedSubjectDto : markedSubjectDtos) {
            if (markedSubjectDto.getIsFilled()) counter++;
        }
        return counter == markedSubjectDtos.size();
    }

    public List<MarkedSubjectDto> findSubjectWithReportMark(List<Long> subjectIds) {
        List<MarkedSubjectDto> markedSubjectDtos = new ArrayList<>();
        List<Subject> subjects = findAll();
        for (Subject subject : subjects) {
            Boolean isFilled = false;
            for (Long subjectId : subjectIds) {
                if (subject.getId() == subjectId) {
                    isFilled = true;
                    markedSubjectDtos.add(new MarkedSubjectDto(subject.getId(), subject.getName(), isFilled));
                }
            }
            if (!isFilled) {
                markedSubjectDtos.add(new MarkedSubjectDto(subject.getId(), subject.getName(), isFilled));
            }
        }

        return markedSubjectDtos;
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Subject subject) {
        Long id = subject.getId();
        Optional<Subject> subjectFromDB = subjectRepository.findById(id);
        if (!subjectFromDB.isEmpty()) {
            return subjectRepository.save(subject);
        } else {
            return null;
        }
    }

    public boolean deleteSubject(Long id) {
        Optional<Subject> subjectFromDB = subjectRepository.findById(id);
        if (!subjectFromDB.isEmpty()) {
            subjectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
