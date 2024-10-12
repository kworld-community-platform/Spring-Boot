package com.hyunjin.kworld.jwt;

import com.hyunjin.kworld.global.MemberDetailsServiceImpl;
import com.hyunjin.kworld.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final MemberDetailsServiceImpl memberDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenProvider.getJwtFromHeader(httpServletRequest,TokenProvider.AUTHORIZATION_HEADER);
        String refreshToken = tokenProvider.getJwtFromHeader(httpServletRequest,TokenProvider.REFRESH_HEADER);

        if (StringUtils.hasText(accessToken)) {

            if (!tokenProvider.validateToken(accessToken)) {
                String refresh = httpServletRequest.getHeader(tokenProvider.REFRESH_HEADER);
                if (!tokenProvider.validateToken(refreshToken) || !refreshTokenRepository.existsByToken(refresh)){
                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new JwtException("리프레시 토큰 오류");
                }

                Claims info = tokenProvider.getUserInfoFromToken(refreshToken);
                String email = info.getSubject();
                accessToken = tokenProvider.createToken(email);
                httpServletResponse.addHeader(TokenProvider.AUTHORIZATION_HEADER, accessToken);
                accessToken = tokenProvider.substringToken(accessToken);
            }
            Claims info = tokenProvider.getUserInfoFromToken(accessToken);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}