package com.hyunjin.kworld.diary.service;

import com.hyunjin.kworld.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
//
//    @Transactional
//    public Diary
}
