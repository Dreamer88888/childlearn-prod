package com.childlearn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentDto {

    private Long id;
    private String title;
    private Date deadline;
    private Long subjectId;

    private List<String> studentName;
    private List<Long> studentId;
    private List<Long> answerSheetId;
    private List<Integer> score;
    private List<String> status;

}
