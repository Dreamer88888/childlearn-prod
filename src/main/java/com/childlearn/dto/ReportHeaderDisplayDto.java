package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportHeaderDisplayDto {

    private String fullName;
    private String base64;
    private String className;
    private List<String> createdDate;
    private List<String> summary;
    private List<Long> reportHeaderIds;

}
