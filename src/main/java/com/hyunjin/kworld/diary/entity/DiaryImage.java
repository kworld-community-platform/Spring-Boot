package com.hyunjin.kworld.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class DiaryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    public DiaryImage(String imageUrl, Diary diary) {
        this.imageUrl = imageUrl;
        this.diary = diary;
    }
}
