package com.heypets.heypetsserver.domain.user.entity;


import com.heypets.heypetsserver.core.mixin.TimestampMixin;
import com.heypets.heypetsserver.domain.user.vo.Role;
import com.heypets.heypetsserver.domain.user.vo.SocialType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Map;

@Getter
@Setter
public class User extends TimestampMixin {
    public static final String TABLENAME = "Users";

    private String _id;
    private String nickname;
    private String email;
    private String password;
    private String profilePath;
    private Boolean isAlarm;
    private List<Map<String,Object>> pets;
    private Boolean unreadAlarm;
    private List<String> searchHistory;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;
}
