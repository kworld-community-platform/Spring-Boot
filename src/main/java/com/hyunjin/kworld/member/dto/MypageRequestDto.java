package com.hyunjin.kworld.member.dto;

import com.hyunjin.kworld.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MypageRequestDto {
    private String name;
    private String profileImage;
    private Gender gender;
    private String studentNumber;
    private String major;
}
