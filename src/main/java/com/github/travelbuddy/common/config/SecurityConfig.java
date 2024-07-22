package com.github.travelbuddy.common.config;

import com.github.travelbuddy.users.jwt.JWTFilter;
import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

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
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource(){
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                //허용할 method
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                //Authorization에 jwt를 넣어서 보내주기 때문에 허용
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }))
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/user/signup").permitAll()
                        .requestMatchers("/api/user/signup/sms/send").permitAll()
                        .requestMatchers("/api/user/sms-code/check").permitAll()
                        .requestMatchers("/api/user/password/*").permitAll()
                        .requestMatchers("/api/boards").permitAll()
                        .requestMatchers("/api/boards/*").permitAll()
                        .requestMatchers("/ws").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtUtill), LoginFilter.class) //loginFilter전에 등록
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtill);
        loginFilter.setFilterProcessesUrl("/api/user/login");

        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
