package com.hyunjin.kworld.diary.repository;

import com.hyunjin.kworld.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {
    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.images i WHERE d.id = :diaryId ORDER BY i.order ASC")
    Optional<Diary> findByIdWithImagesOrdered(@Param("diaryId") Long diaryId);

    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.images i WHERE d.member.id = :memberId ORDER BY d.createdAt DESC, i.order ASC")
    List<Diary> findAllByMemberIdWithImagesOrdered(@Param("memberId") Long memberId);
}

