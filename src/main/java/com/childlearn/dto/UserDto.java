package com.childlearn.dto;

import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.ImageFileTypeConstraint;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String fullName;

    @Size(min = 8, max = 13, message = "Minimal 8 karakter dan maksimal 13 karakter")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Tanggal lahir harus sebelum tanggal saat ini")
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private Date dob;

    @NotNull(message = "File tidak boleh kosong")
    @FileSizeConstraint
    @ImageFileTypeConstraint
    private MultipartFile file;

}
