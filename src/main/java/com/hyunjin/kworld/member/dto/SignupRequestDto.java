package com.hyunjin.kworld.member.dto;

import com.hyunjin.kworld.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private Gender gender;
    private String studentNumber;
    private String major;
}
