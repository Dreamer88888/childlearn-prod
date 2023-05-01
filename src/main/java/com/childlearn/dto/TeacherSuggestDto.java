package com.childlearn.dto;

import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.ImageFileTypeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSuggestDto {

    @NotBlank(message = "Caption tidak boleh kosong")
    private String caption;

    @NotNull(message = "File tidak boleh kosong")
    @FileSizeConstraint
    @ImageFileTypeConstraint
    private MultipartFile file;

}
