package com.github.travelbuddy.users.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.travelbuddy.chat.service.ChatUserService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.RefreshEntity;
import com.github.travelbuddy.users.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtill jwtUtill;
    private final ChatUserService chatUserService;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("attemptAuthentication");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println(email + " " + password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        //authenticationManager를 사용하여 인증을 시도 -> UserDetailsService(사용자 정보 로드)
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)  {

        log.info("로그인 성공 필터");
        CustomUserDetails customUserDetails =(CustomUserDetails) authentication.getPrincipal();

        Integer userId = customUserDetails.getUserId();

//        String token = jwtUtill.createJwt(userId,5*60*60*1000L);

        String token = jwtUtill.createJwt("access", userId,5*60*1000L);
        String refresh = jwtUtill.createJwt("refresh", userId, 5*60*60*1000L);

//        response.setHeader("access", access);
//        response.addCookie(createCookie("refresh",refresh));
//        response.setStatus(HttpStatus.OK.value());

        try {
//            Map<String, String> responseBody = new HashMap<>();
//            responseBody.put("message", "로그인에 성공하였습니다.");
//            responseBody.put("refresh", "Bearer "+refresh);

            response.getWriter().write("Bearer " + refresh);

            response.addHeader("Authorization", "Bearer " + token);

            Date date = new Date(System.currentTimeMillis()+10*60*60*1000L);

            RefreshEntity refreshEntity = new RefreshEntity();
            refreshEntity.setUserId(userId);
            refreshEntity.setRefresh(refresh);
            refreshEntity.setExpiration(date.toString());
            refreshRepository.save(refreshEntity);

//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResponse = objectMapper.writeValueAsString(responseBody);

//            response.setContentType("application/json;charset=UTF-8");

            chatUserService.addUser(token);

//            response.getWriter().write(jsonResponse);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("successful authentication");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)  {

        System.out.println("unsuccessful authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "로그인에 실패하였습니다.");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseBody);

            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write(jsonResponse);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
