package com.hyunjin.kworld.diary.dto;

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
    private List<String> images;

    public DiaryResponseDto(Diary diary){
        this.diaryId = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.images = diary.getImages().stream().map(DiaryImage::getImageUrl).collect(Collectors.toList());
    }
}
