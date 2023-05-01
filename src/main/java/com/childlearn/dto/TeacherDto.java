package com.childlearn.dto;

import com.childlearn.entity.Position;
import com.childlearn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {

    private Long id;
    private User user;
    private Position position;

}
