package com.childlearn.dto;

import com.childlearn.entity.Class;
import com.childlearn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long id;
    private Class cl;
    private User user;

}
