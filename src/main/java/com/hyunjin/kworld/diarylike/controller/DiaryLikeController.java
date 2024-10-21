package com.hyunjin.kworld.diarylike.controller;

import com.hyunjin.kworld.diarylike.service.DiaryLikeService;
import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryLikeController {
    private final DiaryLikeService diaryLikeService;

    @PutMapping("/likes/{diaryId}")
    public ResponseEntity<String> updateLike(@PathVariable Long diaryId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        String message = diaryLikeService.updateLike(diaryId, member);
        return ResponseEntity.ok(message);
    }
}
