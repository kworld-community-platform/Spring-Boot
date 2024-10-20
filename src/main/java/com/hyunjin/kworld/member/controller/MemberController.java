package com.hyunjin.kworld.member.controller;

import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.jwt.TokenProvider;
import com.hyunjin.kworld.member.dto.*;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup (@RequestBody SignupRequestDto signupRequestDto){
        memberService.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login (@RequestBody LoginRequestDto loginRequestDto){
        MemberResponseDto memberResponseDto = memberService.login(loginRequestDto);
        String accessToken = tokenProvider.createToken(memberResponseDto.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(memberResponseDto.getEmail());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("member", memberResponseDto);
        responseBody.put("accessToken", accessToken);
        responseBody.put("refreshToken", refreshToken);
        responseBody.put("message", "로그인에 성공하였습니다.");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDto> getMyPage (@AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        MypageResponseDto mypageResponseDto = memberService.getMyPage(member);
        return ResponseEntity.ok(mypageResponseDto);
    }

    @GetMapping("/intro")
    public ResponseEntity<IntroResponseDto> getIntro (@AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        IntroResponseDto introResponseDto = memberService.getIntro(member);
        return ResponseEntity.ok(introResponseDto);
    }

    @PutMapping("/mypage")
    public ResponseEntity<MypageResponseDto> updateMyPage (@RequestBody MypageRequestDto mypageRequestDto, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        MypageResponseDto mypageResponseDto = memberService.updateMyPage(mypageRequestDto, member);
        return ResponseEntity.ok(mypageResponseDto);
    }

    @PutMapping("/intro")
    public ResponseEntity<IntroResponseDto> updateIntro (@RequestBody IntroRequestDto introRequestDto, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        Member member = MemberDetails.getMember();
        IntroResponseDto introResponseDto = memberService.updateIntro(introRequestDto, member);
        return ResponseEntity.ok(introResponseDto);
    }
}
