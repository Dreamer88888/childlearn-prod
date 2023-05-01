package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDisplayDto {

    private String base64;
    private String fullName;
    private String createdBy;
    private String createdDate;
    private String summary;
    private List<String> subjectNames;
    private List<Integer> scores;
    private List<String> notes;
    private List<String> reportBase64;

}
