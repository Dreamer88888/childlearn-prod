package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_assignment")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long classId;
    private Long teacherId;
    private Long subjectId;
    private Date deadline;

    @Lob
    @Column(length = 100000)
    private byte[] file;

}
