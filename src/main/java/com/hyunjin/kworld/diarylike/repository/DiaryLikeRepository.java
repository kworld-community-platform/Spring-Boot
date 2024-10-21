package com.hyunjin.kworld.diarylike.repository;

import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diarylike.entity.DiaryLike;
import com.hyunjin.kworld.member.entity.Member;

import java.util.Optional;

public interface DiaryLikeRepository {
    Optional<DiaryLike> findByDiaryAndMember(Diary diary, Member member);
}
