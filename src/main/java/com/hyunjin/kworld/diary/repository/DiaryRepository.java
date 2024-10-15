package com.hyunjin.kworld.diary.repository;

import com.hyunjin.kworld.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
}
