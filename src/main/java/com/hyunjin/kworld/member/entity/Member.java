package com.hyunjin.kworld.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String profileImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String major;

    private Boolean isDeleted = false;

    public Member(String email, String password, String name, Gender gender, String studentNumber, String major) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.major = major;
    }
}
