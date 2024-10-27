package com.hyunjin.kworld.comment.dto;

import com.hyunjin.kworld.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long memberId;
    private String comment;
    private String writer;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.comment = comment.getComment();
        this.writer = comment.getMember().getName();
        this.createdAt = comment.getCreatedAt();
    }
}
