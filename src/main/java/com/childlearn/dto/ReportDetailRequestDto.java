package com.childlearn.dto;

import com.childlearn.validator.ReportFileSizeConstraint;
import com.childlearn.validator.ReportImageFileTypeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailRequestDto {

    private String subjectName;
    private Long subjectId;
    private Long studentId;

    @NotBlank(message = "Catatan tidak boleh kosong")
    private String notes;

    @NotNull(message = "Nilai tidak boleh kosong")
    private Integer score;

    @ReportFileSizeConstraint
    @ReportImageFileTypeConstraint
    private MultipartFile file;

    private Long reportHeaderId;

    private String rtdBase64;

    public ReportDetailRequestDto(String subjectName, Long subjectId, Long studentId, Long reportHeaderId) {
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.studentId = studentId;
        this.reportHeaderId = reportHeaderId;
    }

}
