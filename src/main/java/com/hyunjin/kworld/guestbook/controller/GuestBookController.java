package com.hyunjin.kworld.guestbook.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.guestbook.dto.GuestBookResponseDto;
import com.hyunjin.kworld.guestbook.service.GuestBookService;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guestbooks")
public class GuestBookController {
    private final GuestBookService guestBookService;

    @PostMapping
    public ResponseEntity<GuestBookResponseDto> createGuestBook (@RequestParam String title,
                                                                 @RequestParam String content,
                                                                 @RequestPart(name = "image", required = false) MultipartFile image,
                                                                 @AuthenticationPrincipal MemberDetailsImpl MemberDetails)throws IOException {
        Member member = MemberDetails.getMember();
        GuestBookResponseDto guestBookResponseDto = guestBookService.createGuestBook(title, content, image, member);
        return ResponseEntity.ok(guestBookResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<GuestBookResponseDto>> getAllGuestBook (@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<GuestBookResponseDto> guestBookResponseDtos = guestBookService.getAllGuestBook(pageable);
        return ResponseEntity.ok(guestBookResponseDtos);
    }
}
