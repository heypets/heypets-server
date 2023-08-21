package com.heypets.heypetsserver.core.mixin;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimestampNotDeletedMixin {
    private Timestamp createdAt = Timestamp.now();
    private Timestamp updatedAt = Timestamp.now();
    private Boolean status = true;
}
