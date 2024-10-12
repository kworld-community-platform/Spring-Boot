package com.hyunjin.kworld.member.service;

import com.hyunjin.kworld.member.dto.SignupRequestDto;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signup (SignupRequestDto signupRequestDto){
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String confirmPassword = signupRequestDto.getConfirmPassword();
        String name = signupRequestDto.getName();

        if(!password.equals(confirmPassword)){
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }

        if(memberRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("이미 가입한 계정입니다.");
        }

        Member member = new Member(email, password, name);

        memberRepository.save(member);
    }
}
