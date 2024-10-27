package com.hyunjin.kworld.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuestBookRequestDto {
    private String title;
    private String content;
    private String guestBookImage;
}
