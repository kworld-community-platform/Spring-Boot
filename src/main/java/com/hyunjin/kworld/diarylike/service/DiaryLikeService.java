package com.hyunjin.kworld.diarylike.service;

import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diary.repository.DiaryRepository;
import com.hyunjin.kworld.diarylike.entity.DiaryLike;
import com.hyunjin.kworld.diarylike.repository.DiaryLikeRepository;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryLikeService {
    private final DiaryLikeRepository diaryLikeRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public String updateLike(Long diaryId, Member member){
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(()->new IllegalArgumentException("다이어리가 존재하지 않습니다."));

        DiaryLike existingLike = diaryLikeRepository.findByDiaryAndMember(diary,member);

        if(existingLike != null){
            diaryLikeRepository.delete(existingLike);
            diary.removeLike();
            return "하트 제거";
        }

        DiaryLike newLike = new DiaryLike(diary,member);
        diaryLikeRepository.save(newLike);
        diary.addLike();
        return "하트 추가";
    }
}
