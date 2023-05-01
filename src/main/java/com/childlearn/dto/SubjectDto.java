package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectDto {

    @NotBlank(message = "Nama mata pelajaran tidak boleh kosong")
    private String name;

}
