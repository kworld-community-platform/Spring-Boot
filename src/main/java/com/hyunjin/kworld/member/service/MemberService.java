package com.hyunjin.kworld.member.service;

import com.hyunjin.kworld.jwt.TokenProvider;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.dto.MemberResponseDto;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
import com.hyunjin.kworld.member.entity.Gender;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import com.hyunjin.kworld.member.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void signup (SignupRequestDto signupRequestDto){
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String confirmPassword = signupRequestDto.getConfirmPassword();
        String name = signupRequestDto.getName();
        Gender gender = signupRequestDto.getGender();
        String studentNumber = signupRequestDto.getStudentNumber();
        String major = signupRequestDto.getMajor();

        if(!password.equals(confirmPassword)){
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }

        if(memberRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("이미 가입한 계정입니다.");
        }

        Member member = new Member(email, passwordEncoder.encode(password), name, gender, studentNumber, major);

        memberRepository.save(member);
    }

    @Transactional
    public MemberResponseDto login (LoginRequestDto loginrequestDto){
        Member member = memberRepository.findByEmail(loginrequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보를 확인해주세요"));

        if (!passwordEncoder.matches(loginrequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("로그인 정보를 확인해주세요");
        }

        return new MemberResponseDto(
                member.getEmail(),
                member.getName(),
                member.getProfileImage(),
                member.getGender(),
                member.getStudentNumber(),
                member.getMajor()
        );
    }
}
