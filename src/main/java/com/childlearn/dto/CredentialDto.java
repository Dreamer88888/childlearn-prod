package com.childlearn.dto;

import com.childlearn.validator.CredentialConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@CredentialConstraint
public class CredentialDto {

    @NotBlank(message = "Nama pengguna tidak boleh kosong")
    private String username;

    @NotBlank(message = "Kata sandi tidak boleh kosong")
    private String password;

}

