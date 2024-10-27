package com.hyunjin.kworld.member.service;

import com.hyunjin.kworld.jwt.TokenProvider;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.dto.MemberResponseDto;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import com.hyunjin.kworld.member.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){

    }

    @Test
    @DisplayName("회원가입 성공")
    public void signupSuccess(){
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr", "1234", "1234", "조광운");
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupRequestDto.getPassword())).thenReturn("encodedPassword");
        memberService.signup(signupRequestDto);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패/비밀번호 불일치")
    void signupPasswordMismatch() {
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr", "성공", "실패", "조광운");

        assertThatThrownBy(() -> memberService.signup(signupRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        Member member = new Member("test@kw.ac.kr", "encodedPassword", "조광운");

        when(memberRepository.findByEmail("test@kw.ac.kr")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("1234", member.getPassword())).thenReturn(true);
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@kw.ac.kr", "1234");
        MemberResponseDto memberResponseDto = memberService.login(loginRequestDto);
        assertThat(memberResponseDto.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("로그인 실패/비밀번호 불일치")
    void loginPasswordMismatch() {
        Member member = new Member("test@kw.ac.kr", "encodedPassword", "조광운");
        when(memberRepository.findByEmail("test@kw.ac.kr")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("12345", member.getPassword())).thenReturn(false);
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@kw.ac.kr", "12345");
        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로그인 정보를 확인해주세요");
    }
}
