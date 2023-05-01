package com.childlearn.dto;

import com.childlearn.entity.Class;
import com.childlearn.entity.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialDto {

    private Long id;
    private String title;
    private Class cl;
    private Subject subject;

    @Lob
    @Column(length = 100000)
    private byte[] file;

}
