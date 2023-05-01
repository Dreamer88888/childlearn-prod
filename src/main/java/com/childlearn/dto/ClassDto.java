package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClassDto {

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String name;

    @NotNull(message = "Wali kelas tidak boleh kosong")
    private Long teacherId;

}
