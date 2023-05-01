package com.childlearn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {

    @NotNull(message = "Nilai tidak boleh kosong")
    private Integer score;

    private Long answerSheetId;

}
