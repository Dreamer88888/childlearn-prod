package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDisplayDto {

    private Long id;
    private String fullName;
    private String base64;
    private String phoneNumber;
    private String positionName;

    public UserDisplayDto(Long id, String fullName, String base64) {
        this.id = id;
        this.fullName = fullName;
        this.base64 = base64;
    }

    public UserDisplayDto(String fullName, String base64) {
        this.fullName = fullName;
        this.base64 = base64;
    }

}
