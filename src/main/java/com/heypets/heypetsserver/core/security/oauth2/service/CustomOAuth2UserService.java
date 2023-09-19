package com.heypets.heypetsserver.core.security.oauth2.service;

import com.heypets.heypetsserver.core.security.oauth2.CustomOAuth2User;
import com.heypets.heypetsserver.core.security.oauth2.OAuthAttributes;
import com.heypets.heypetsserver.domain.user.entity.User;
import com.heypets.heypetsserver.domain.user.repository.UserRepository;
import com.heypets.heypetsserver.domain.user.vo.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 완료");

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        System.out.println(oAuth2User);
        System.out.println(attributes);

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        System.out.println(extractAttributes);
        System.out.println(extractAttributes.getNameAttributeKey());
        System.out.println(extractAttributes.getOauth2UserInfo());
        System.out.println(extractAttributes.getOauth2UserInfo().getId());
        System.out.println(extractAttributes.getOauth2UserInfo().getImageUrl());
        System.out.println(extractAttributes.getOauth2UserInfo().getId());
        System.out.println(extractAttributes.getOauth2UserInfo().getNickname());
        User createdUser = null; // getUser() 메소드로 User 객체 생성 후 반환
        try {
            createdUser = getUser(extractAttributes, socialType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) throws Exception{
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findUser == null) {
            log.info("응 유저 없는 데?");
            return saveUser(attributes, socialType);
        }
        log.info("응 유저 있는 데? "+findUser.toString());
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) throws Exception{
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser).orElse(null);
    }
}
