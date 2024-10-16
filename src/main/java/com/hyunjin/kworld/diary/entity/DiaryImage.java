package com.hyunjin.kworld.diary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "image_order", nullable = false)
    private int order;

    public DiaryImage(String imageUrl, Diary diary) {
        this.imageUrl = imageUrl;
        this.diary = diary;
    }

    public DiaryImage(String imageUrl, Diary diary, int order) {
        this.imageUrl = imageUrl;
        this.diary = diary;
        this.order = order;
    }

    public void updateOrder(int order) {
        this.order = order;
    }
}
