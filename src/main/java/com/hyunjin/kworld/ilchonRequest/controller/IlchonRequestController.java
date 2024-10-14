package com.hyunjin.kworld.ilchonRequest.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.ilchonRequest.dto.IlchonResponseDto;
import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import com.hyunjin.kworld.ilchonRequest.service.IlchonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ilchon")
public class IlchonRequestController {
    private final IlchonRequestService ilchonRequestService;

    @PostMapping("/request/{receiverId}")
    public ResponseEntity<IlchonResponseDto> request (@PathVariable Long receiverId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        IlchonRequest ilchonRequest = ilchonRequestService.request(receiverId,MemberDetails.getMember());
        IlchonResponseDto ilchonResponseDto = new IlchonResponseDto(ilchonRequest.getId(),ilchonRequest.getRequester().getName(),ilchonRequest.getRequester().getGender().toString(),ilchonRequest.getRequester().getStudentNumber(),ilchonRequest.getRequester().getMajor(),ilchonRequest.getStatus());
        return ResponseEntity.ok(ilchonResponseDto);
    }

    @GetMapping("/response")
    public ResponseEntity<List<IlchonResponseDto>> getRequests (@AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        List<IlchonResponseDto> ilchonRequestList = ilchonRequestService.getRequests(MemberDetails.getMember());
        return ResponseEntity.ok(ilchonRequestList);
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptRequest (@PathVariable Long requestId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        ilchonRequestService.acceptRequest(requestId,MemberDetails.getMember());
        return ResponseEntity.ok().build();
    }
}
