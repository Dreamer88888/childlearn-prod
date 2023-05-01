package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String phoneNumber;
    private String role;
    private Date dob;
    private String fullName;

    @Lob
    @Column(length = 100000)
    private byte[] file;

    public User (Long id, String fullName, byte[] file, String phoneNumber, String position) {
        this.id = id;
        this.fullName = fullName;
        this.file = file;
        this.phoneNumber = phoneNumber;
        this.role = position;
    }

    public User(String fullName, byte[] file) {
        this.fullName = fullName;
        this.file = file;
    }

}
