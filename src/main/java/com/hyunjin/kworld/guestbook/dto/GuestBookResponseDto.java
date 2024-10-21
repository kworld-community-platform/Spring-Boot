package com.hyunjin.kworld.guestbook.dto;

import com.hyunjin.kworld.guestbook.entity.GuestBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuestBookResponseDto {
    private Long guestBookId;
    private String title;
    private String content;
    private String guestBookImage;
    private String writer;

    public GuestBookResponseDto(GuestBook guestBook){
        this.guestBookId = guestBook.getId();
        this.title = guestBook.getTitle();
        this.content = guestBook.getContent();
        this.guestBookImage = guestBook.getGuestBookImage();
        this.writer = guestBook.getWriter().getName();
    }
}
