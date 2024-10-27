package com.hyunjin.kworld.member.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.dto.SignupRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("회원가입 성공")
    public void signupSuccess() throws Exception{
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr", "1234", "1234", "조광운");
        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Success"));
    }

    @Test
    @DisplayName("로그인 성공")
    public void loginSuccess() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr", "1234", "1234", "조광운");
        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated());

        LoginRequestDto loginRequestDto = new LoginRequestDto("test@kw.ac.kr", "1234");

        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void loginPasswordMismatch() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@kw.ac.kr", "1234", "1234", "조광운");
        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated());

        LoginRequestDto loginRequestDto = new LoginRequestDto("test@kw.ac.kr", "wrongPassword");

        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인 정보를 확인해주세요"));
    }
}
