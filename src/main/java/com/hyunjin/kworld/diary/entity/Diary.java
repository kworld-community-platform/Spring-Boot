package com.hyunjin.kworld.diary.entity;

import com.hyunjin.kworld.comment.entity.Comment;
import com.hyunjin.kworld.diarylike.entity.DiaryLike;
import com.hyunjin.kworld.global.BaseEntity;
import com.hyunjin.kworld.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DiaryImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryLike> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Diary(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
        this.images =new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void addImage(DiaryImage image) {
        this.images.add(image);
        reorderImages();
    }

    public void removeImage(DiaryImage image) {
        images.remove(image);
        reorderImages();
    }

    public void update(String newTitle, String newContent) {
        if (newTitle != null && !newTitle.isBlank()) {
            this.title = newTitle;
        }
        if (newContent != null && !newContent.isBlank()) {
            this.content = newContent;
        }
    }

    public void reorderImages() {
        for (int i = 0; i < images.size(); i++) {
            images.get(i).updateOrder(i); // 순서를 i로 설정
        }
    }

    public int getLikeCount() {
        return likes.size();
    }
}
