package com.childlearn.service;

import com.childlearn.dto.TeacherSuggestDisplayDto;
import com.childlearn.dto.TeacherSuggestDto;
import com.childlearn.entity.TeacherSuggest;
import com.childlearn.repository.TeacherSuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeacherSuggestService {

    @Autowired
    private TeacherSuggestRepository teacherSuggestRepository;

    public List<TeacherSuggestDisplayDto> findAll() {
        List<TeacherSuggestDisplayDto> teacherSuggests = teacherSuggestRepository.findAll().stream().map(teacherSuggest -> {

            String base64 = Base64.getEncoder().encodeToString(teacherSuggest.getFile());

            return new TeacherSuggestDisplayDto(teacherSuggest.getId(), teacherSuggest.getCaption(), base64, teacherSuggest.getType());
        }).collect(Collectors.toList());

        return teacherSuggests;
    }

    public Optional<TeacherSuggest> findById(Long id) {
        return teacherSuggestRepository.findById(id);
    }

    public String getBase64Image(Long id) {
        Optional<TeacherSuggest> teacherSuggest = findById(id);
        String imageString = "";
        if (teacherSuggest.isPresent()) {
            imageString = Base64.getEncoder().encodeToString(teacherSuggest.get().getFile());
        }
        return imageString;
    }

    public void createTeacherSuggest(TeacherSuggestDto teacherSuggestDto) throws IOException {

        MultipartFile file = teacherSuggestDto.getFile();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        TeacherSuggest teacherSuggest = new TeacherSuggest();
        teacherSuggest.setCaption(teacherSuggestDto.getCaption());
        teacherSuggest.setFile(file.getBytes());
        teacherSuggest.setType(file.getContentType());

        teacherSuggestRepository.save(teacherSuggest);
    }

    public boolean updateTeacherSuggest(TeacherSuggest teacherSuggest) {
        if (teacherSuggestRepository.findById(teacherSuggest.getId()).isPresent()) {
            teacherSuggestRepository.save(teacherSuggest);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteTeacherSuggest(Long id) {
        if (teacherSuggestRepository.findById(id).isPresent()) {
            teacherSuggestRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
