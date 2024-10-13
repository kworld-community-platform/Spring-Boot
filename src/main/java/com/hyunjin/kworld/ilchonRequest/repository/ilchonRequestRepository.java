package com.hyunjin.kworld.ilchonRequest.repository;

import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ilchonRequestRepository extends JpaRepository<IlchonRequest,Long> {
}
