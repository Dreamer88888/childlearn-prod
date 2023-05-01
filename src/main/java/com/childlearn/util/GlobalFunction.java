package com.childlearn.util;

import com.childlearn.dto.ReportTempDto;
import com.childlearn.entity.User;
import com.childlearn.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class GlobalFunction {

    @Autowired
    private static UserService userService;

    public static String getRandomImgPath() {
        String[] datas = {"ehsan", "upin", "ipin", "susanti", "mail", "meimei"};
        int i = ThreadLocalRandom.current().nextInt(0, datas.length);

        return datas[i] + ".png";
    }

    public static String generateCustomCookie(User user) {
        String plainCookie = user.getId() + "-" + user.getRole() + "-" + user.getFullName();

        return Base64.getEncoder().encodeToString(plainCookie.getBytes());
    }

    public static String getUserId(HttpServletRequest request) {
        return getCustomCookie(request).get(0);
    }

    public static String getUserRole(HttpServletRequest request) {
        return getCustomCookie(request).get(1);
    }

    public static String getUserFullName(HttpServletRequest request) {
        return getCustomCookie(request).get(2);
    }

    public static String getUserBase64(HttpServletRequest request) {
        Long userId = Long.valueOf(getUserId(request));

        return userService.findBase64ByUserId(userId);
    }

    public static List<String> getCustomCookie(HttpServletRequest request) {
        Optional<String> encodedCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "CUSTOM_COOKIE".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();

        byte[] decodedBytes = Base64.getDecoder().decode(encodedCookie.get());

        String decodedCookie = new String(decodedBytes);

        List<String> userDetails = List.of(decodedCookie.split("-"));

        return userDetails;
    }

    public static String convertLocaleIndonesiaDate(Date dateToBeConvert) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String dateString = formatter.format(dateToBeConvert);

        return dateString.replace("January", "Januari").replace("February", "Februari")
                .replace("March", "Maret").replace("May", "Mei").replace("June", "Juni")
                .replace("July", "Juli").replace("August", "Agustus").replace("October", "Oktober")
                .replace("December", "Desember");
    }
    
}
