package com.hyunjin.kworld.ilchon.repository;

import com.hyunjin.kworld.ilchon.entity.Ilchon;
import com.hyunjin.kworld.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IlchonRepository extends JpaRepository<Ilchon,Long> {
    boolean existsByMember1AndMember2(Member member1, Member member2);

    @Query("SELECT i FROM Ilchon i WHERE (i.member1.id = :member1Id AND i.member2.id = :member2Id) OR (i.member1.id = :member2Id AND i.member2.id = :member1Id)")
    Optional<Ilchon> findByMemberIds(@Param("member1Id") Long member1Id, @Param("member2Id") Long member2Id);
}
