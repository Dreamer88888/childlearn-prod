package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDisplayDto {

    private Long id;
    private String date;
    private Integer count;
    private List<String> subjectNames;
    private List<String> titles;

}
