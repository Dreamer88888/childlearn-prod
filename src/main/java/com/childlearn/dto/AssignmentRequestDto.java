package com.childlearn.dto;

import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.PdfFileTypeConstraint;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDto {

    @NotBlank(message = "Judul tidak boleh kosong")
    private String title;

    @NotNull(message = "Mata pelajaran tidak boleh kosong")
    private Long subjectId;

    @NotNull(message = "Kelas tidak boleh kosong")
    private Long classId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Tenggat waktu tidak boleh kosong")
    @FutureOrPresent(message = "Tenggat waktu harus lebih dari atau hari ini")
    private Date deadline;

    @NotNull(message = "File tidak boleh kosong")
    @FileSizeConstraint
    @PdfFileTypeConstraint
    private MultipartFile file;

}
