package com.childlearn.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class AgendaRequestDto {

    private Long classId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Tanggal harus lebih dari atau hari ini")
    @NotNull(message = "Tanggal tidak boleh kosong")
    private Date date;

    @NotNull(message = "Mata pelajaran tidak boleh kosong")
    private Long subjectId;

    @NotBlank(message = "Judul tidak boleh kosong")
    private String title;

}
