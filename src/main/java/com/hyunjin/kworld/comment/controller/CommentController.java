package com.hyunjin.kworld.comment.controller;

import com.hyunjin.kworld.comment.dto.CommentRequestDto;
import com.hyunjin.kworld.comment.dto.CommentResponseDto;
import com.hyunjin.kworld.comment.service.CommentService;
import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/diaries/{diaryId}")
    public ResponseEntity<CommentResponseDto> createComment (@PathVariable Long diaryId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        CommentResponseDto commentResponseDto = commentService.createComment(diaryId, commentRequestDto, member);
        return ResponseEntity.ok(commentResponseDto);
    }

    @PutMapping("/{diaryId}/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment (@PathVariable Long diaryId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        CommentResponseDto commentResponseDto = commentService.updateComment(diaryId, commentId, commentRequestDto, member);
        return ResponseEntity.ok(commentResponseDto);
    }
}
