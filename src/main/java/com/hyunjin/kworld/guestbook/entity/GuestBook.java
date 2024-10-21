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
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    public GuestBook(String title, String content, String guestBookImage, Member owner, Member writer) {
        this.title = title;
        this.content = content;
        this.guestBookImage = guestBookImage;
        this.owner = owner;
        this.writer = writer;
    }

    public GuestBook update(String title, String content, String guestBookImage) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (guestBookImage != null) {
            this.guestBookImage = guestBookImage;
        }
        return this;
    }
}
