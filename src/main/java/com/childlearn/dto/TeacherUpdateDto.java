package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpdateDto {

    private Long id;
    private Long userId;

    @NotNull(message = "Posisi tidak boleh kosong")
    private Long positionId;

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    private String fullName;

}
