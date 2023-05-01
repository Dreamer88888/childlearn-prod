package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAsgDto {

    private Long id;
    private String title;
    private Date deadline;
    private Long subjectId;
    private Long answerSheetId;
    private Integer score;
    private String status;

}
