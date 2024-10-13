package com.hyunjin.kworld.ilchonRequest.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.ilchonRequest.dto.IlchonResponseDto;
import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import com.hyunjin.kworld.ilchonRequest.service.IlchonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ilchon")
public class IlchonRequestController {
    private final IlchonRequestService ilchonRequestService;

    @PostMapping("/request/{receiverId}")
    public ResponseEntity<IlchonResponseDto> request (@PathVariable Long receiverId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        IlchonRequest ilchonRequest = ilchonRequestService.request(receiverId,MemberDetails.getMember());
        IlchonResponseDto ilchonResponseDto = new IlchonResponseDto(ilchonRequest.getId(),ilchonRequest.getStatus());
        return ResponseEntity.ok(ilchonResponseDto);
    }
}
