package com.hyunjin.kworld.guestbook.repository;

import com.hyunjin.kworld.guestbook.entity.GuestBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    Page<GuestBook> findByOwnerId(Long ownerId, Pageable pageable);
}
