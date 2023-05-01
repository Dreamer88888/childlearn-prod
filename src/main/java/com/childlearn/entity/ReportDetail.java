package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_report_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reportHeaderId;
    private Long subjectId;
    private Integer score;
    private String notes;

    @Lob
    @Column(length = 100000)
    private byte[] file;

}
