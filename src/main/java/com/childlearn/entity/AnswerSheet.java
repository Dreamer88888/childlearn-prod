package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_answer_sheet")
@Data
@AllArgsConstructor @NoArgsConstructor
public class AnswerSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long assignmentId;
    private Integer score;
    private Date uploadedDate;

    @Lob
    @Column(length = 100000)
    private byte[] file;

}
