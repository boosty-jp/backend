package co.jp.wever.graphql.infrastructure.converter.entity.article;

import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseEntityConverter {
    public static ArticleBaseEntity toArticleBaseEntity(ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .title(articleBase.getTitle())
                                .description(articleBase.getDescription())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().name())
                                .createdDate(articleBase.getDate().getCreateDate())
                                .updatedDate(articleBase.getDate().getUpdateDate())
                                .build();
    }

    public static ArticleBaseEntity toArticleBaseEntity(String articleId,ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .id(articleId)
                                .title(articleBase.getTitle())
                                .description(articleBase.getDescription())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().name())
                                .createdDate(articleBase.getDate().getCreateDate())
                                .updatedDate(articleBase.getDate().getUpdateDate())
                                .build();
    }
}
