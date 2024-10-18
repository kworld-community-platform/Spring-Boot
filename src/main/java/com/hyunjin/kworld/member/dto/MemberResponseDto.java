package com.hyunjin.kworld.member.dto;

import com.hyunjin.kworld.member.entity.Gender;
import com.hyunjin.kworld.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String email;
    private String name;
    private String profileImage;
    private Gender gender;
    private String studentNumber;
    private String major;

    public MemberResponseDto(Member member){
        this.email = member.getEmail();
        this.name = member.getName();
        this.profileImage = this.getProfileImage();
        this.gender = member.getGender();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
    }
}