package com.hyunjin.kworld.ilchon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IlchonResponseDto {
    private long otherMemberId;
    private String otherMemberName;
    private String otherMemberGender;
    private String otherMemberStudentNumber;
    private String otherMemberMajor;
}
