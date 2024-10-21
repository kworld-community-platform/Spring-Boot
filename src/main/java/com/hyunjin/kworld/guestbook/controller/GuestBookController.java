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

@RestController
@RequiredArgsConstructor
@RequestMapping("/guestbooks")
public class GuestBookController {
    private final GuestBookService guestBookService;

    @PostMapping("/mini/{ownerId}")
    public ResponseEntity<GuestBookResponseDto> createGuestBook(@PathVariable Long ownerId,
                                                                @RequestParam String title,
                                                                @RequestParam String content,
                                                                @RequestPart(name = "image", required = false) MultipartFile image,
                                                                @AuthenticationPrincipal MemberDetailsImpl MemberDetails) throws IOException {
        Member writer = MemberDetails.getMember();
        GuestBookResponseDto guestBookResponseDto = guestBookService.createGuestBook(ownerId, title, content, image, writer);
        return ResponseEntity.ok(guestBookResponseDto);
    }

    @GetMapping("/mini/{ownerId}")
    public ResponseEntity<Page<GuestBookResponseDto>> getAllGuestBook(@PathVariable Long ownerId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GuestBookResponseDto> guestBookResponseDtos = guestBookService.getAllGuestBook(ownerId,pageable);
        return ResponseEntity.ok(guestBookResponseDtos);
    }

    @GetMapping("/{guestbookId}")
    public ResponseEntity<GuestBookResponseDto> getOneGuestBook(@PathVariable Long guestbookId,
                                                                @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        Member writer = MemberDetails.getMember();
        GuestBookResponseDto guestbookResponseDto = guestBookService.getOneGuestBook(guestbookId, writer);
        return ResponseEntity.ok(guestbookResponseDto);
    }

    @PutMapping("/{guestbookId}")
    public ResponseEntity<GuestBookResponseDto> updateGuestBook(@PathVariable Long guestbookId,
                                                                @RequestParam(required = false) String title,
                                                                @RequestParam(required = false) String content,
                                                                @RequestPart(name = "image", required = false) MultipartFile image,
                                                                @AuthenticationPrincipal MemberDetailsImpl MemberDetails) throws IOException {
        Member writer = MemberDetails.getMember();
        GuestBookResponseDto guestBookResponseDto = guestBookService.updateGuestBook(guestbookId, title, content, image, writer);
        return ResponseEntity.ok(guestBookResponseDto);
    }

    @DeleteMapping("/{guestbookId}")
    public ResponseEntity<String> deleteGuestbook(@PathVariable Long guestbookId,
                                                  @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member writer = MemberDetails.getMember();
        guestBookService.deleteGuestBook(guestbookId, writer);
        return ResponseEntity.ok("방명록이 삭제되었습니다.");
    }
}
