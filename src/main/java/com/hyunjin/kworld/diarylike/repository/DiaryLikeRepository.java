package com.hyunjin.kworld.diarylike.repository;

import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diarylike.entity.DiaryLike;
import com.hyunjin.kworld.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryLikeRepository extends JpaRepository<DiaryLike, Long> {
    DiaryLike findByDiaryAndMember(Diary diary, Member member);
}
