package com.heypets.heypetsserver.domain.alarmhistory.entity;

import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.core.mixin.TimestampNotDeletedMixin;
import com.heypets.heypetsserver.domain.alarmhistory.vo.AlarmCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmHistory extends TimestampNotDeletedMixin {
    public static final String TABLENAME = AlarmHistory.class.getSimpleName();

    private String _id;
    private DocumentReference userId;
    private DocumentReference eventDetailId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private AlarmCategory alarmCategory;
}
