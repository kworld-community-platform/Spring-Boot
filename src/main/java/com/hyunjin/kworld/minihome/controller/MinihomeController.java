package com.hyunjin.kworld.minihome.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.minihome.dto.MinihomeResponseDto;
import com.hyunjin.kworld.minihome.service.MinihomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/minihome")
public class MinihomeController {
    private final MinihomeService minihomeService;

    @GetMapping("/{ownerId}")
    public ResponseEntity<MinihomeResponseDto> getMinihome (@PathVariable Long ownerId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member loginMember = MemberDetails.getMember();
        MinihomeResponseDto minihomeResponseDto = minihomeService.getMinihome(ownerId, loginMember);
        return ResponseEntity.ok(minihomeResponseDto);
    }
}
