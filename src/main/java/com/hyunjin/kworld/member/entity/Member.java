package com.hyunjin.kworld.member.entity;

import com.hyunjin.kworld.member.dto.MypageRequestDto;
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String studentNumber;

    private String major;

    private String intro;

    private Boolean isDeleted = false;

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void update(MypageRequestDto mypageRequestDto){
        if(mypageRequestDto.getName() != null){
            this.name = mypageRequestDto.getName();
        }
        if(mypageRequestDto.getProfileImage() != null){
            this.profileImage = mypageRequestDto.getProfileImage();
        }
        if(mypageRequestDto.getGender() != null){
            this.gender = mypageRequestDto.getGender();
        }
        if(mypageRequestDto.getStudentNumber() != null){
            this.studentNumber = mypageRequestDto.getStudentNumber();
        }
        if(mypageRequestDto.getMajor() != null){
            this.major = mypageRequestDto.getMajor();
        }
    }
}
