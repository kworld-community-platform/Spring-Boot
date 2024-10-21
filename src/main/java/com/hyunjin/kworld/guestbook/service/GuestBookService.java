package com.hyunjin.kworld.guestbook.service;

import com.hyunjin.kworld.global.S3Uploader;
import com.hyunjin.kworld.guestbook.dto.GuestBookResponseDto;
import com.hyunjin.kworld.guestbook.entity.GuestBook;
import com.hyunjin.kworld.guestbook.repository.GuestBookRepository;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GuestBookService {
    private final GuestBookRepository guestBookRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public GuestBookResponseDto createGuestBook(Long ownerId, String title, String content, MultipartFile image, Member writer)throws IOException {
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        String guestBookImage = null;

        if(image != null && !image.isEmpty()){
            guestBookImage = s3Uploader.uploadFile(image);
        }

        GuestBook guestBook = new GuestBook(title,content,guestBookImage, owner, writer);
        guestBookRepository.save(guestBook);
        return new GuestBookResponseDto(guestBook);
    }

    @Transactional(readOnly = true)
    public Page<GuestBookResponseDto> getAllGuestBook (Long ownerId, Pageable pageable){
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<GuestBook> guestBooks = guestBookRepository.findByOwnerId(ownerId,sortedPageable);
        return guestBooks.map(GuestBookResponseDto::new);
    }

    @Transactional
    public GuestBookResponseDto getOneGuestBook(Long guestbookId, Member member){
        GuestBook guestBook = guestBookRepository.findById(guestbookId)
                .orElseThrow(() -> new IllegalArgumentException("방명록이 존재하지 않습니다."));

        if (!guestBook.getWriter().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return new GuestBookResponseDto(guestBook);
    }

    @Transactional
    public GuestBookResponseDto updateGuestBook(Long guestbookId, String title, String content, MultipartFile image, Member writer) throws IOException {
        GuestBook guestBook = guestBookRepository.findById(guestbookId)
                .orElseThrow(() -> new IllegalArgumentException("방명록이 존재하지 않습니다."));

        if (!guestBook.getWriter().getId().equals(writer.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        String guestBookImage = guestBook.getGuestBookImage();

        if (image != null && !image.isEmpty()) {
            if (guestBookImage != null) {
                s3Uploader.deleteFile(guestBookImage);
            }
            guestBookImage = s3Uploader.uploadFile(image);
        }

        guestBook.update(title, content, guestBookImage);
        guestBookRepository.save(guestBook);
        return new GuestBookResponseDto(guestBook);
    }
}
