package com.childlearn.service;

import com.childlearn.dto.NewsletterDisplayDto;
import com.childlearn.dto.NewsletterRequestDto;
import com.childlearn.entity.Newsletter;
import com.childlearn.entity.User;
import com.childlearn.repository.NewsletterRepository;
import com.childlearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private UserRepository userRepository;

    public List<NewsletterDisplayDto> findAll() {
        List<NewsletterDisplayDto> newsletterDisplayDtos = newsletterRepository.findAll().stream().map(newsletter -> {
            String base64 = Base64.getEncoder().encodeToString(newsletter.getFile());
            Optional<User> user = userRepository.findById(newsletter.getUserId());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String dateString = formatter.format(newsletter.getActivityDate());

            return new NewsletterDisplayDto(newsletter.getId(), newsletter.getTitle(), dateString, user.get(), base64);
        }
        ).collect(Collectors.toList());

        return newsletterDisplayDtos;
    }

    public Optional<Newsletter> findById(Long id) {
        return newsletterRepository.findById(id);
    }

    public void createNewsletter(NewsletterRequestDto newsletterRequestDto) throws IOException {
        MultipartFile file = newsletterRequestDto.getFile();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Newsletter newsletter = new Newsletter();

        newsletter.setCreatedDate(new Date());
        newsletter.setActivityDate(newsletterRequestDto.getActivityDate());
        newsletter.setFile(file.getBytes());
        newsletter.setType(file.getContentType());
        newsletter.setTitle(newsletterRequestDto.getTitle());
        newsletter.setUserId(newsletterRequestDto.getTeacherId());

        newsletterRepository.save(newsletter);
    }

    public boolean updateNewsletter(Newsletter newsletter) {
        if (newsletterRepository.findById(newsletter.getId()).isPresent()) {
            newsletterRepository.save(newsletter);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteNewsletter(Long id) {
        if (newsletterRepository.findById(id).isPresent()) {
            newsletterRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
