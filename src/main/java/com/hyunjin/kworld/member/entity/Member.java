package com.hyunjin.kworld.member.entity;

import com.hyunjin.kworld.diarylike.entity.DiaryLike;
import com.hyunjin.kworld.member.dto.IntroRequestDto;
import com.hyunjin.kworld.member.dto.MypageRequestDto;
import com.hyunjin.kworld.member.dto.MypageResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String intro;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryLike> likes = new ArrayList<>();

    public Member(String email, String password, String name, Gender gender, String studentNumber, String major) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.major = major;
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

    public void updateIntro(IntroRequestDto introRequestDto){
        this.intro = introRequestDto.getIntro();
    }

    public int getLikeCount() {
        return likes.size();
    }
}
