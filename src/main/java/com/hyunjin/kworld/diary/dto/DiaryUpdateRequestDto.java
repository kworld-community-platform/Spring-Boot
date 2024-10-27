package com.hyunjin.kworld.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryUpdateRequestDto {
    private String title;
    private String content;
    private List<MultipartFile> newImages;
    private List<Long> deleteImgIds;
    private List<Long> replaceImgIds;
    private List<MultipartFile> replaceImages;
}
