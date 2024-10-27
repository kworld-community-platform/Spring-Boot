package com.hyunjin.kworld.member.dto;

import com.hyunjin.kworld.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroResponseDto {
    private String intro;

    public IntroResponseDto(Member member){
        this.intro = member.getIntro();
    }
}
