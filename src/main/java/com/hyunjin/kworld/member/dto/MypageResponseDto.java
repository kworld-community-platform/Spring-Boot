package com.hyunjin.kworld.member.dto;

import com.hyunjin.kworld.member.entity.Gender;
import com.hyunjin.kworld.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MypageResponseDto {
    private String name;
    private String profileImage;
    private Gender gender;
    private String studentNumber;
    private String major;
    private String intro;

    public MypageResponseDto(Member member){
        this.name = member.getName();
        this.profileImage = member.getProfileImage();
        this.gender = member.getGender();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
        this.intro = member.getIntro();
    }
}
