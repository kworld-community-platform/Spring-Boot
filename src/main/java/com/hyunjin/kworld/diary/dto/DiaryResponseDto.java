package com.hyunjin.kworld.diary.dto;

import com.hyunjin.kworld.diary.entity.DiaryImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DiaryResponseDto {
    private Long diaryId;
    private String title;
    private String content;
    private List<String> images;
}
