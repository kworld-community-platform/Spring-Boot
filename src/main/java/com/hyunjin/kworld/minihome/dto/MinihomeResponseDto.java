package com.hyunjin.kworld.minihome.dto;

import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungResponseDto;
import com.hyunjin.kworld.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MinihomeResponseDto {
    private String ownerName;
    private String profileImage;
    private String intro;
    private String gender;
    private String studentNumber;
    private String major;
    private List<IlchonpyungResponseDto> ilchonpyungs;
    private boolean isIlchon;

    public MinihomeResponseDto(Member owner, List<IlchonpyungResponseDto>ilchonpyungs, boolean isIlchon){
        this.ownerName = owner.getName();
        this.profileImage = owner.getProfileImage();
        this.intro = owner.getIntro();
        this.gender = owner.getGender().toString();
        this.studentNumber = owner.getStudentNumber();
        this.major = owner.getMajor();
        this.ilchonpyungs = ilchonpyungs;
        this.isIlchon = isIlchon;
    }
}
