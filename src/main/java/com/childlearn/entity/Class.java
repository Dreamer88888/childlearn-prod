package com.childlearn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String name;

    @NotNull(message = "Wali kelas tidak boleh kosong")
    private Long teacherId;

    public Class(String name, Long teacherId) {
        this.name = name;
        this.teacherId = teacherId;
    }

}
