package com.heypets.heypetsserver.core.security.oauth2;

import com.heypets.heypetsserver.core.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.heypets.heypetsserver.core.security.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.heypets.heypetsserver.core.security.oauth2.userinfo.NaverOAuth2UserInfo;
import com.heypets.heypetsserver.core.security.oauth2.userinfo.OAuth2UserInfo;
import com.heypets.heypetsserver.domain.user.entity.User;
import com.heypets.heypetsserver.domain.user.vo.Role;
import com.heypets.heypetsserver.domain.user.vo.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        System.out.println("엔티티 email:"+oauth2UserInfo.getEmail());

        User user = new User();
        user.setSocialType(socialType);
        user.setSocialId(oauth2UserInfo.getId());
        user.setEmail(oauth2UserInfo.getEmail());
        user.setNickname(oauth2UserInfo.getNickname());
        user.setProfilePath(oauth2UserInfo.getImageUrl());
        user.setRole(Role.ROLE_GUEST);
        return user;

    }
}
