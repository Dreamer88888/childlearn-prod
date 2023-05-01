package com.childlearn.dto;

import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.ImageFileTypeConstraint;
import com.childlearn.validator.PdfFileTypeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class MaterialRequestDto {

    @NotBlank(message = "Judul tidak boleh kosong")
    private String title;

    @NotNull(message = "Kelas tidak boleh kosong")
    private Long classId;

    @NotNull(message = "Mata pelajaran tidak boleh kosong")
    private Long subjectId;

    @NotNull(message = "File tidak boleh kosong")
    @FileSizeConstraint
    @PdfFileTypeConstraint
    private MultipartFile file;

}
