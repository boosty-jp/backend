package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseEntityConverter {
    public static ArticleBaseEntity toArticleBaseEntity(ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .title(articleBase.getTitle())
                                .description(articleBase.getDescription())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().name())
                                .createdDate(articleBase.getDate().getUnixCreateDate())
                                .updatedDate(articleBase.getDate().getUnixUpdateDate())
                                .build();
    }

    public static ArticleBaseEntity toArticleBaseEntity(String articleId, ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .id(articleId)
                                .title(articleBase.getTitle())
                                .description(articleBase.getDescription())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().name())
                                .createdDate(articleBase.getDate().getUnixCreateDate())
                                .updatedDate(articleBase.getDate().getUnixUpdateDate())
                                .build();
    }

    public static ArticleBaseEntity toArticleBaseEntity(
        Map<Object, Object> articleVertex, String status) {
        return ArticleBaseEntity.builder()
                                .id(VertexConverter.toString("title", articleVertex))
                                .imageUrl(VertexConverter.toString("imageUrl", articleVertex))
                                .description(VertexConverter.toString("description", articleVertex))
                                .title(VertexConverter.toString("title", articleVertex))
                                .status("draft") // TODO: あとでなおす
                                .createdDate(VertexConverter.toLong("createDate", articleVertex))
                                .updatedDate(VertexConverter.toLong("updateDate", articleVertex))
                                .build();
    }
}
