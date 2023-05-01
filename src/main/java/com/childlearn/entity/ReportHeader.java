package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_report_header")
@Data
@AllArgsConstructor @NoArgsConstructor
public class ReportHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private String period;
    private Date createdDate;
    private String createdBy;
    private String summary;

}
