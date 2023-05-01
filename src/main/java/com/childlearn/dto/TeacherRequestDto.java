package com.childlearn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeacherRequestDto {

    @NotNull(message = "Nama lengkap tidak boleh kosong")
    private Long userId;

    @NotNull(message = "Posisi tidak boleh kosong")
    private Long positionId;

}
