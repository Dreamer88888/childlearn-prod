package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_newsletter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;
    private Date activityDate;
    private Date createdDate;
    private String type;

    @Lob
    @Column(length = 100000)
    private byte[] file;

}
