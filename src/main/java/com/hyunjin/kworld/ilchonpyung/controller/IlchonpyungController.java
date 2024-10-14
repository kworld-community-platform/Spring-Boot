package com.hyunjin.kworld.ilchonpyung.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungRequestDto;
import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungResponseDto;
import com.hyunjin.kworld.ilchonpyung.service.IlchonpyungService;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("ilchonpyung")
public class IlchonpyungController {
    private final IlchonpyungService ilchonpyungService;

    @PostMapping("/{ownerId}")
    public ResponseEntity<IlchonpyungResponseDto> write (@PathVariable Long ownerId, @RequestBody IlchonpyungRequestDto ilchonpyungRequestDto, @AuthenticationPrincipal MemberDetailsImpl MemberDetails) {
        Member writerId = MemberDetails.getMember();
        IlchonpyungResponseDto ilchonpyungResponseDto = ilchonpyungService.write(ownerId, writerId, ilchonpyungRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ilchonpyungResponseDto);
    }
}
