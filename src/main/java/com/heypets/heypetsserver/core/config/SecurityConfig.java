package com.heypets.heypetsserver.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heypets.heypetsserver.core.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.heypets.heypetsserver.core.security.jwt.service.JwtService;
import com.heypets.heypetsserver.core.security.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.heypets.heypetsserver.core.security.login.handler.LoginFailureHandler;
import com.heypets.heypetsserver.core.security.login.handler.LoginSuccessHandler;
import com.heypets.heypetsserver.core.security.login.util.LoginService;
import com.heypets.heypetsserver.core.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.heypets.heypetsserver.core.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.heypets.heypetsserver.core.security.oauth2.service.CustomOAuth2UserService;
import com.heypets.heypetsserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .headers((f)->
                        f.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .sessionManagement((session)->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**","/error").permitAll()
                                .requestMatchers("/sign-up","/oauth2/sign-up","/domain/**").permitAll()
                                .requestMatchers("/swagger-ui/**","/swagger-ui.html","/swagger-resources/**","/v3/api-docs/**","/api-docs/*").permitAll()
                                .requestMatchers("/**").authenticated()
                )
                .oauth2Login((user)->user
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint((userinfo)->userinfo.userService(customOAuth2UserService))
                );
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(),LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, userRepository);
    }
}