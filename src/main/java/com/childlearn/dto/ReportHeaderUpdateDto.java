package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportHeaderUpdateDto {

    private String studentName;

    @NotBlank(message = "Rangkuman tidak boleh kosong")
    private String summary;

    private Long subjectId;
    private Long studentId;
    private Long reportHeaderId;

}