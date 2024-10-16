package com.hyunjin.kworld.ilchonpyung.repository;

import com.hyunjin.kworld.ilchonpyung.entity.Ilchonpyung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IlchonpyungRepository extends JpaRepository<Ilchonpyung, Long> {
    List<Ilchonpyung> findAllByOwnerId(Long ownerId);
    boolean existsByOwnerIdAndWriterId(Long ownerId, Long writerId);
}
