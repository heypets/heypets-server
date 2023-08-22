package com.heypets.heypetsserver.domain.pet.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.core.mixin.TimestampMixin;
import com.heypets.heypetsserver.domain.pet.vo.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Pet extends TimestampMixin {
    public static final String TABLENAME = Pet.class.getSimpleName();

    private String _id;
    private DocumentReference OwnerId;
    private String name;
    private List<Map<String,Object>> guests;
    private Timestamp birthday;
    private String profilePath;

    private String inviteCode;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
