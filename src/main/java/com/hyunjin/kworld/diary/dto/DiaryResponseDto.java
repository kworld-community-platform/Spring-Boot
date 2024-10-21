package com.hyunjin.kworld.diary.dto;

import com.hyunjin.kworld.comment.dto.CommentResponseDto;
import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diary.entity.DiaryImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class DiaryResponseDto {
    private Long diaryId;
    private String title;
    private String content;
    private int likeCount;
    private List<ImageDto> images;
    private List<CommentResponseDto> comments;

    public DiaryResponseDto(Diary diary){
        this.diaryId = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.images = diary.getImages().stream()
                .map(img -> new ImageDto(img.getId(), img.getImageUrl()))
                .collect(Collectors.toList());
        this.comments = diary.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public DiaryResponseDto(Long diaryId, String title, String content, int likeCount, List<ImageDto> images){
        this.diaryId = diaryId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.images = images;
        this.comments = new ArrayList<>();
    }
}
