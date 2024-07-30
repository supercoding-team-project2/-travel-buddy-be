package com.github.travelbuddy.users.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
//    try {

//        if (jwtUtill.isExpired(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        try {
            jwtUtill.isExpired(token);
        }catch (ExpiredJwtException e) {

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "access 토큰이 만료되었습니다.");

            PrintWriter writer = response.getWriter();
            writer.println("access token expired");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseBody);

            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write(jsonResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtill.getCategory(token);
        if(!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer userId = jwtUtill.getUserId(token);

        System.out.println("userId: " + userId);
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(userId)
                .build();

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

//    }catch (SignatureException e) {
//        System.err.println("Invalid token");
//    }
        filterChain.doFilter(request, response);
    }
}
