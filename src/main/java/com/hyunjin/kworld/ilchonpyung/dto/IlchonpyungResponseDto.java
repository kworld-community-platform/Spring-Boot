package com.hyunjin.kworld.ilchonpyung.dto;

import com.hyunjin.kworld.ilchonpyung.entity.Ilchonpyung;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IlchonpyungResponseDto {
    private Long id;
    private String nickname;
    private String ilchonpyung;

    public IlchonpyungResponseDto(Ilchonpyung ilchonpyung) {
        this.id = ilchonpyung.getId();
        this.nickname = ilchonpyung.getNickname();
        this.ilchonpyung = ilchonpyung.getIlchonpyung();
    }
}
