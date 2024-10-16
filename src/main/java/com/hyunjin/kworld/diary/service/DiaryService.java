package com.hyunjin.kworld.diary.service;

import com.hyunjin.kworld.diary.dto.DiaryRequestDto;
import com.hyunjin.kworld.diary.dto.DiaryResponseDto;
import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diary.entity.DiaryImage;
import com.hyunjin.kworld.diary.repository.DiaryRepository;
import com.hyunjin.kworld.global.S3Uploader;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public DiaryResponseDto createDiary (List<MultipartFile> images, DiaryRequestDto diaryRequestDto, Member member) {
        Diary diary = new Diary(diaryRequestDto.getTitle(), diaryRequestDto.getContent(), member);
        diaryRepository.save(diary);

        List<String> imageUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            try {
                imageUrls = s3Uploader.uploadFiles(images);

                for (String imageUrl : imageUrls) {
                    DiaryImage diaryImage = new DiaryImage(imageUrl, diary);
                    diary.addImage(diaryImage);
                }
            } catch (IOException e) {
                throw new RuntimeException("S3 파일 업로드 중 오류가 발생했습니다.", e);
            }
        }
        return new DiaryResponseDto(diary.getId(), diary.getTitle(), diary.getContent(), imageUrls);
    }
}