package com.hyunjin.kworld.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.kworld.global.MemberDetailsImpl;
import com.hyunjin.kworld.member.dto.LoginRequestDto;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.entity.RefreshToken;
import com.hyunjin.kworld.member.repository.MemberRepository;
import com.hyunjin.kworld.member.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public AuthenticationFilter(TokenProvider tokenProvider,RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository){
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        setFilterProcessesUrl("/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto = new ObjectMapper().readValue(httpServletRequest.getInputStream(), LoginRequestDto.class);

            Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
            if(member != null && !member.getIsDeleted()){
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.getEmail(),
                                loginRequestDto.getPassword(),
                                null
                        )
                );
            }else {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(httpServletResponse, "존재하지 않는 회원입니다.");
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = tokenProvider.createToken(email);
        String refresh = tokenProvider.createRefreshToken(email);


        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(null);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(refresh, email);
        } else {
            refreshToken.updateToken(refresh);
        }
        refreshTokenRepository.save(refreshToken);
        httpServletResponse.addHeader(TokenProvider.AUTHORIZATION_HEADER, token);
        httpServletResponse.addHeader(TokenProvider.REFRESH_HEADER, refreshToken.getToken());
        httpServletResponse.setContentType("application/json; charset=UTF-8");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("refreshToken", refreshToken.getToken());

        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(responseBody));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException failed) {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writeResponse(httpServletResponse, "아이디 또는 비밀번호가 틀렸습니다.");
    }

    private void writeResponse(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
        }
    }
}