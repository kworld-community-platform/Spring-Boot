package com.hyunjin.kworld.member.controller;

import com.hyunjin.kworld.jwt.TokenProvider;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.dto.MemberResponseDto;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
import com.hyunjin.kworld.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
