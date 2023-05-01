package com.childlearn.dto;

import com.childlearn.validator.PasswordConstraint;
import com.childlearn.validator.PasswordMatchConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PasswordConstraint
@PasswordMatchConstraint
public class ManagePasswordDto {

    private Long userId;

    @NotBlank(message = "Kata sandi saat ini tidak boleh kosong")
    private String currentPassword;

    @NotBlank(message = "Kata sandi baru tidak boleh kosong")
    @Size(min = 8, message = "Kata sandi minimal terdiri dari 8 karakter")
    private String newPassword;

    @NotBlank(message = "Konfirmasi kata sandi baru tidak boleh kosong")
    private String confirmNewPassword;

    public ManagePasswordDto(Long userId) {
        this.userId = userId;
    }

}
