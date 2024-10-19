package com.hyunjin.kworld.guestbook.entity;

import com.hyunjin.kworld.global.BaseEntity;
import com.hyunjin.kworld.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuestBook extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String guestBookImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public GuestBook(String title, String content, String guestBookImage, Member member) {
        this.title = title;
        this.content = content;
        this.guestBookImage = guestBookImage;
        this.member = member;
    }
}
