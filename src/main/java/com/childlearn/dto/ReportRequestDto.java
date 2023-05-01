package com.childlearn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {

    private Long reportHeaderId;
    private String studentName;

    @NotBlank(message = "Rangkuman tidak boleh kosong")
    private String summary;

    private Long studentId;
    private Date createdDate;
    private boolean isAllFilled;
    private String rtdBase64;

}
