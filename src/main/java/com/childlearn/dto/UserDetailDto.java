package com.childlearn.dto;

import com.childlearn.entity.Class;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {

    private String role;
    private String fullName;
    private List<Class> cls;
    private String base64;
    private Long userId;

    public UserDetailDto(String role, String fullName) {
        this.role = role;
        this.fullName = fullName;
    }

    public UserDetailDto(String role, String fullName, List<Class> cls) {
        this.role = role;
        this.fullName = fullName;
        this.cls = cls;
    }

}
