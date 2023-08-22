package com.heypets.heypetsserver.domain.event.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.core.mixin.TimestampMixin;
import com.heypets.heypetsserver.domain.event.vo.EventType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Event extends TimestampMixin {
    public static final String TABLENAME = Event.class.getSimpleName();

    private String _id;
    private DocumentReference petId;
    private String title;
    private String content;
    private String location;

    private Boolean isAlarm;

    private Timestamp startDatetime;
    private Timestamp endDatetime;

    private List<Map<String,Object>> images;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

}
