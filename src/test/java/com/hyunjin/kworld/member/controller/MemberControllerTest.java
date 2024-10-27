package com.hyunjin.kworld.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.kworld.jwt.TokenProvider;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.dto.MemberResponseDto;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
import com.hyunjin.kworld.member.entity.Gender;
import com.hyunjin.kworld.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenProvider tokenProvider;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilters(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    @Test
    @DisplayName("회원가입/201/응답")
    public void signup() throws Exception{
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr","12kwukwu","12kwukwu","조광운");
        doNothing().when(memberService).signup(any(SignupRequestDto.class));

        mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Success"));

        verify(memberService, Mockito.times(1)).signup(any(SignupRequestDto.class));
    }

    @Test
    @DisplayName("로그인/200/응답")
    public void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@kw.ac.kr", "12kwukwu");
        MemberResponseDto memberResponseDto = new MemberResponseDto("test@kw.ac.kr", "조광운", null, Gender.MALE, null, null);
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        // any()를 사용하여 인자 매칭
        when(memberService.login(any(LoginRequestDto.class))).thenReturn(memberResponseDto);
        when(tokenProvider.createToken(memberResponseDto.getEmail())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(memberResponseDto.getEmail())).thenReturn(refreshToken);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("member", memberResponseDto);
        expectedResponse.put("accessToken", accessToken);
        expectedResponse.put("refreshToken", refreshToken);
        expectedResponse.put("message", "Success");

        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));

        verify(memberService, Mockito.times(1)).login(any(LoginRequestDto.class));
    }
}