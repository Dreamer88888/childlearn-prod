package com.childlearn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgendaDetailRequestDto {

    private Long agendaHeaderId;
    private Long subjectId;
    private String title;

}
