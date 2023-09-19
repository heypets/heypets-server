package com.heypets.heypetsserver.core.security.oauth2.handler;

import com.heypets.heypetsserver.core.security.jwt.service.JwtService;
import com.heypets.heypetsserver.core.security.oauth2.CustomOAuth2User;
import com.heypets.heypetsserver.domain.user.entity.User;
import com.heypets.heypetsserver.domain.user.repository.UserRepository;
import com.heypets.heypetsserver.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        log.info("request",request.getRequestURI());
        log.info("request",request.getRequestURL());

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            log.info("기록");
            System.out.println(oAuth2User.getAttributes());

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if(oAuth2User.getRole() == Role.ROLE_GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                log.info("access-token: "+"Bearer " + accessToken);
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("/oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
                findUser.authorizeUser();
                userRepository.update(findUser);

                jwtService.sendAccessAndRefreshToken(response, accessToken, null);

            } else {
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성

            }
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws Exception {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
//        response.sendRedirect("http://localhost:8080");

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
