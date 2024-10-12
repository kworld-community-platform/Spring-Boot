package com.hyunjin.kworld.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilters(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    @Test
    @DisplayName("회원가입 요청 시 회원가입 처리와 상태코드 201 반환")
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
}
