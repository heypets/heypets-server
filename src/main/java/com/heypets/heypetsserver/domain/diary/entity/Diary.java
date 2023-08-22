package com.heypets.heypetsserver.domain.diary.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.core.mixin.TimestampMixin;
import com.heypets.heypetsserver.domain.diary.vo.Tag;
import com.heypets.heypetsserver.domain.diary.vo.Weather;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Diary extends TimestampMixin {
    public static final String TABLENAME = Diary.class.getSimpleName();

    private List<Map<String,Object>> imagePaths;
    private String title;
    private Timestamp date;
    private String content;

    private DocumentReference petId;

    @Enumerated(EnumType.STRING)
    private Weather weather;

    @Enumerated(EnumType.STRING)
    private Tag tag;
}
