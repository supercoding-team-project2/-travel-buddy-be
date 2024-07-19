package com.github.travelbuddy.users.jwt;

import com.github.travelbuddy.users.dto.CustomUserDetails;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtill jwtUtill;

@Override
protected void doFilterInternal(HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull FilterChain filterChain) throws ServletException, IOException {

    System.out.println("JWTFilter");
    String authorization = request.getHeader("Authorization");

    //Authorization 헤더 검증
    if(authorization == null || !authorization.startsWith("Bearer ")) {
        System.out.println("token null");

        //request,response를 다음 필터로 넘겨줌
        filterChain.doFilter(request, response);

        return;
    }

    //Bearer부분 제거
    String token = authorization.split(" ")[1];
    try {

        if (jwtUtill.isExpired(token)) {

            filterChain.doFilter(request, response);
            return;
        }

        Integer userId = jwtUtill.getUserId(token);

        System.out.println("userId: " + userId);
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(userId)
                .build();

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

    }catch (SignatureException e) {
        System.err.println("Invalid token");
    }
        filterChain.doFilter(request, response);
}
}
