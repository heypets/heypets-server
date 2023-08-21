package com.heypets.heypetsserver.domain.eventdetail.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.domain.eventdetail.vo.CLoopType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDetail {
    private String _id;
    private DocumentReference eventId;

    @Enumerated(EnumType.STRING)
    private CLoopType cLoopType;

    private String cAlertNo;

    private Timestamp cStartDateTime;
    private Timestamp cEndDateTime;
}
