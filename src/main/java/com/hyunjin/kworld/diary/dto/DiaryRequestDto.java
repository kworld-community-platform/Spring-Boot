package com.hyunjin.kworld.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@AllArgsConstructor
public class DiaryRequestDto {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
