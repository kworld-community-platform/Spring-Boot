package com.hyunjin.kworld.ilchon.repository;

import com.hyunjin.kworld.ilchon.entity.Ilchon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ilchonRepository extends JpaRepository<Ilchon,Long> {
}
