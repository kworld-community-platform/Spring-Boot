package com.hyunjin.kworld.comment.service;

import com.hyunjin.kworld.comment.dto.CommentRequestDto;
import com.hyunjin.kworld.comment.dto.CommentResponseDto;
import com.hyunjin.kworld.comment.entity.Comment;
import com.hyunjin.kworld.comment.repository.CommentRepository;
import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diary.repository.DiaryRepository;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final DiaryRepository diaryRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Long diaryId, CommentRequestDto commentRequestDto, Member member) {
        Diary diary = diaryRepository.findByIdWithImagesOrdered(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("다이어리가 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto.getComment(), diary, member);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }
}
