package com.childlearn.repository;

import com.childlearn.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {

    Optional<Newsletter> findById(Long id);

    void deleteById(Long id);

}
