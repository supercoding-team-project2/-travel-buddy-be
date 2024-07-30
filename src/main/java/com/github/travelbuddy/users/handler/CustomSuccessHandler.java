package com.github.travelbuddy.users.handler;

import com.github.travelbuddy.users.dto.oauth2.CustomOAuth2User;
import com.github.travelbuddy.users.jwt.JWTUtill;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtill jwtUtill;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Integer userId = customUserDetails.getUserId();

        String token = jwtUtill.createJwt(userId,60*60*1000L);

        response.sendRedirect("http://localhost:3000/oauth2-jwt?token=Bearer "+token);
    }
}
