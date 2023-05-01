package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto {

    private Long id;

    @NotNull(message = "Kelas tidak boleh kosong")
    private Long classId;
    private Long userId;

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    private String fullName;

}
