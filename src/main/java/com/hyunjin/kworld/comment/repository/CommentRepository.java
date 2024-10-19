package com.hyunjin.kworld.comment.repository;

import com.hyunjin.kworld.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
