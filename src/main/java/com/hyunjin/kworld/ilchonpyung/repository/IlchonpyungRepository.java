package com.hyunjin.kworld.ilchonpyung.repository;

import com.hyunjin.kworld.ilchonpyung.entity.Ilchonpyung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IlchonpyungRepository extends JpaRepository<Ilchonpyung, Long> {
}
