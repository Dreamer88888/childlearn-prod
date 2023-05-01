package com.childlearn.dto;

import com.childlearn.entity.Class;
import com.childlearn.entity.Subject;
import com.childlearn.validator.FileSizeConstraint;
import com.childlearn.validator.PdfFileTypeConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSheetRequestDto {

    @FileSizeConstraint
    @PdfFileTypeConstraint
    private MultipartFile file;

    private Long assignmentId;

}
