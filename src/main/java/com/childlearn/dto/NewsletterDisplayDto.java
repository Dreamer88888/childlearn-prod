package com.childlearn.dto;

import com.childlearn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterDisplayDto {

    private Long id;
    private String title;
    private String activityDateString;
    private User user;
    private String base64;

}
