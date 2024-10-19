package com.hyunjin.kworld.guestbook.service;

import com.hyunjin.kworld.global.S3Uploader;
import com.hyunjin.kworld.guestbook.dto.GuestBookResponseDto;
import com.hyunjin.kworld.guestbook.entity.GuestBook;
import com.hyunjin.kworld.guestbook.repository.GuestBookRepository;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GuestBookService {
    private final GuestBookRepository guestBookRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public GuestBookResponseDto createGuestBook(String title, String content, MultipartFile image, Member member)throws IOException {
        String guestBookImage = null;

        if(image != null && !image.isEmpty()){
            guestBookImage = s3Uploader.uploadFile(image);
        }

        GuestBook guestBook = new GuestBook(title,content,guestBookImage, member);
        guestBookRepository.save(guestBook);
        return new GuestBookResponseDto(guestBook);
    }
}
