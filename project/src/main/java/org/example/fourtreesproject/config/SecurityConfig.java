package org.example.fourtreesproject.config;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.exception.CustomAccessDeniedHandler;
import org.example.fourtreesproject.exception.CustomAuthenticationEntryPoint;
import org.example.fourtreesproject.jwt.JwtUtil;
import org.example.fourtreesproject.jwt.Repository.RefreshTokenRepository;
import org.example.fourtreesproject.oauth.OAuth2AuthenticationSuccessHandler;
import org.example.fourtreesproject.oauth.OAuth2Service;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2Service oAuth2Service;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost/pay.html"); // 허용할 출처
        config.addAllowedOrigin("http://192.168.0.119:3000"); // 허용할 출처
        config.addAllowedOrigin("http://localhost:8080"); // 허용할 출처
        config.addAllowedOrigin("http://192.168.0.119:8080"); // 허용할 출처
        config.addAllowedOriginPattern("*"); // 허용할 출처
        config.addAllowedMethod("*"); // 허용할 메서드 (GET, POST, PUT 등)
        config.addAllowedHeader("*"); // 허용할 헤더
        config.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());
        http.sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin((auth) -> auth.disable());
        http.authorizeHttpRequests((auth) ->
                auth
                        .requestMatchers(
                                "/user/signup", "/user/login", "/user/verify",
                                "/seller/signup", "/coupon/register", "/company-reg/verify").permitAll() // 모든 사람 접속 가능
                        .requestMatchers("/user/delivery/", "/user/info/detail").hasRole("USER")
                        .requestMatchers("/seller/info/detail").hasRole("SELLER")
                        .requestMatchers("/company/**").hasRole("SELLER")
                        .requestMatchers("/product/**").hasRole("SELLER")
                        .requestMatchers("/gpbuy/**").hasRole("USER")
                        .requestMatchers("/bid/**").hasRole("SELLER")
                        .requestMatchers("/orders/**").hasRole("USER")
                        .requestMatchers("/v2/api-dosc", "/swagger-resources/**", "/swagger-ui.html","/webjars/**","/swagger-ui/**" ).permitAll()
                        .anyRequest().permitAll()
        );
        http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(customAccessDeniedHandler));
        http.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint ));

        LoginFilter loginFilter = new LoginFilter(jwtUtil, authenticationManager(authenticationConfiguration), refreshTokenRepository);
        loginFilter.setFilterProcessesUrl("/user/login");

        http.addFilterBefore(new JwtFilter(jwtUtil, refreshTokenRepository), LoginFilter.class);
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login((config) -> {
            config.successHandler(oAuth2AuthenticationSuccessHandler);
            config.userInfoEndpoint((endPoint) -> endPoint.userService(oAuth2Service));
        });

        return http.build();
    }
}
