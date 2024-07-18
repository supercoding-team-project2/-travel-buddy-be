package com.github.travelbuddy.common.config;

import com.github.travelbuddy.users.jwt.JWTFilter;
import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtill jwtUtill;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception {
        return configuration.getAuthenticationManager();
    }

    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {

        http
                .csrf((auth) -> auth.disable());
        http
                .formLogin((auth) -> auth.disable());
        http
                .httpBasic((auth) -> auth.disable());
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/user/signup").permitAll()
                        .anyRequest().authenticated());

//        http
//                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        //jwt는 세션을 stateless로 관리

        //loginFilter전에 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtill), LoginFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtill);
        loginFilter.setFilterProcessesUrl("/api/user/login");

        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
