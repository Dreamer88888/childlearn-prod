package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class TeacherSuggestDisplayDto {

    private Long id;
    private String caption;
    private String base64;
    private String type;

}
