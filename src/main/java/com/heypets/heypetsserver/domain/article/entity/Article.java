package com.heypets.heypetsserver.domain.article.entity;

import com.google.cloud.firestore.DocumentReference;
import com.heypets.heypetsserver.core.mixin.TimestampMixin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Article extends TimestampMixin {
    public static final String TABLENAME = Article.class.getSimpleName();

    private String _id;
    private DocumentReference superuser;
    private String title;
    private String articleImage;
}
