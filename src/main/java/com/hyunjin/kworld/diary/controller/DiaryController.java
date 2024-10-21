package com.hyunjin.kworld.diary.controller;

import com.hyunjin.kworld.diary.dto.DiaryRequestDto;
import com.hyunjin.kworld.diary.dto.DiaryResponseDto;
import com.hyunjin.kworld.diary.dto.DiaryUpdateRequestDto;
import com.hyunjin.kworld.diary.service.DiaryService;
import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<DiaryResponseDto> createDiary (@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("content") String content,
                                                         @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        Member member = MemberDetails.getMember();
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(title, content,images);
        DiaryResponseDto diaryResponseDto = diaryService.createDiary(images, diaryRequestDto, member);
        return ResponseEntity.ok(diaryResponseDto);
    }

    @GetMapping("/members/diaries/{memberId}")
    public ResponseEntity<List<DiaryResponseDto>> getAllDiary (@PathVariable Long memberId){
        List<DiaryResponseDto> diaryResponseDtos = diaryService.getAllDiary(memberId);
        return ResponseEntity.ok(diaryResponseDtos);
    }

    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponseDto> getOneDiary (@PathVariable Long diaryId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        DiaryResponseDto diaryResponseDto = diaryService.getOneDiary(diaryId, member);
        return ResponseEntity.ok(diaryResponseDto);
    }

    @PutMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponseDto> updateDiary(@PathVariable Long diaryId,
                                                        @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
                                                        @RequestParam(value = "deleteIds", required = false) List<Long> deleteImgIds,
                                                        @RequestParam(value = "replaceIds", required = false) List<Long> replaceImgIds,
                                                        @RequestPart(value = "replaceImages", required = false) List<MultipartFile> replaceImages,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String content,
                                                        @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        Member member = memberDetails.getMember();
        DiaryUpdateRequestDto diaryUpdateRequestDto = new DiaryUpdateRequestDto(title, content, newImages, deleteImgIds, replaceImgIds, replaceImages);
        DiaryResponseDto diaryResponseDto = diaryService.updateDiary(diaryId, diaryUpdateRequestDto, member);
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @DeleteMapping("/diaries/{diaryId}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long diaryId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        diaryService.deleteDiary(diaryId, member);
        return ResponseEntity.ok("다이어리를 삭제하셨습니다.");
    }
}
