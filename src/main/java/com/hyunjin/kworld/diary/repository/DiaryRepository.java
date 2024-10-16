package com.hyunjin.kworld.diary.repository;

import com.hyunjin.kworld.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {
    List<Diary> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
