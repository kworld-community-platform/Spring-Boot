package com.hyunjin.kworld.ilchon.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.ilchon.service.IlchonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IlchonController {
    private final IlchonService ilchonService;

    @DeleteMapping("/ilchon/{otherMemberId}")
    public ResponseEntity<String> deleteIlchon (@PathVariable Long otherMemberId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        Long currentMemberId = MemberDetails.getMember().getId();
        ilchonService.deleteIlchon(currentMemberId, otherMemberId);
        return ResponseEntity.ok("일촌을 끊었습니다.");
    }
}
