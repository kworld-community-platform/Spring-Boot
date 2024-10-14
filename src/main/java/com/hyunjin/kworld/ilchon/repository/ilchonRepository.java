package com.hyunjin.kworld.ilchon.repository;

import com.hyunjin.kworld.ilchon.entity.Ilchon;
import com.hyunjin.kworld.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ilchonRepository extends JpaRepository<Ilchon,Long> {
    boolean existsByMember1AndMember2(Member member1, Member member2);
}
