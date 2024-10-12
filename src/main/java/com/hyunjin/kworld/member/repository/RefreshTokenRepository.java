package com.hyunjin.kworld.member.repository;

import com.hyunjin.kworld.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByEmail(String email);
    boolean existsByToken(String refresh);
    int deleteByEmail(String email);
}
