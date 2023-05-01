package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSuggestResponseDto {

    private Long id;
    private String caption;
    private String fileDownloadUri;

}
