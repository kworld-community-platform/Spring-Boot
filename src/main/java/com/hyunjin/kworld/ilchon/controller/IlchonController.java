package com.hyunjin.kworld.ilchon.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.ilchon.dto.IlchonResponseDto;
import com.hyunjin.kworld.ilchon.service.IlchonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ilchon")
public class IlchonController {
    private final IlchonService ilchonService;

    @GetMapping("/getAllIlchon")
    public ResponseEntity<List<IlchonResponseDto>> getAllIlchon (@AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Long currentMemberId = MemberDetails.getMember().getId();
        List<IlchonResponseDto> ilchonList = ilchonService.getAllIlchon(currentMemberId);
        return ResponseEntity.ok(ilchonList);
    }

    @DeleteMapping("/{otherMemberId}")
    public ResponseEntity<String> deleteIlchon (@PathVariable Long otherMemberId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        Long currentMemberId = MemberDetails.getMember().getId();
        ilchonService.deleteIlchon(currentMemberId, otherMemberId);
        return ResponseEntity.ok("일촌을 끊었습니다.");
    }
}
