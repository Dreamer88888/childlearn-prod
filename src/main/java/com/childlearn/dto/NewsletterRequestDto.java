package com.childlearn.dto;

import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.ImageFileTypeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterRequestDto {

    @NotBlank(message = "Judul tidak boleh kosong")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Tanggal kegiatan harus kurang dari atau hari ini")
    @NotNull(message = "Tanggal kegiatan tidak boleh kosong")
    private Date activityDate;

    @NotNull(message = "File tidak boleh kosong")
    @FileSizeConstraint
    @ImageFileTypeConstraint
    private MultipartFile file;

    private Long teacherId;

}
