package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarkedSubjectDto {

    private Long id;
    private String name;
    private Boolean isFilled;

}
