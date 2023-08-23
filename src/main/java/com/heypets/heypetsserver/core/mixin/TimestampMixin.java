package com.heypets.heypetsserver.core.mixin;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimestampMixin {
    private Timestamp createdAt = Timestamp.now();
    private Timestamp updatedAt = Timestamp.now();
    private Timestamp deletedAt = null;
    private Boolean status = true;
}