package com.childlearn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class AgendaHeaderRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Long classId;

}
