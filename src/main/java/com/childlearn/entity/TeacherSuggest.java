package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_teacher_suggest")
@Data
@AllArgsConstructor @NoArgsConstructor
public class TeacherSuggest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    @Lob
    @Column(length = 100000)
    private byte[] file;

    private String type;

}
