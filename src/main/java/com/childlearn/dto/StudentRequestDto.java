package com.childlearn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentRequestDto {

    @NotNull(message = "Kelas tidak boleh kosong")
    private Long classId;

    @NotNull(message = "Nama lengkap tidak boleh kosong")
    private Long userId;

}
