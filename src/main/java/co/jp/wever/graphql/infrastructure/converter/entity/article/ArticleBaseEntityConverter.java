package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseEntityConverter {
    public static ArticleBaseEntity toArticleBaseEntity(ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .title(articleBase.getTitle())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().getString())
                                .createdDate(articleBase.getDate().getUnixCreateDate())
                                .updatedDate(articleBase.getDate().getUnixUpdateDate())
                                .build();
    }

    public static ArticleBaseEntity toArticleBaseEntity(String articleId, ArticleBase articleBase) {
        return ArticleBaseEntity.builder()
                                .id(articleId)
                                .title(articleBase.getTitle())
                                .imageUrl(articleBase.getImageUrl())
                                .status(articleBase.getStatus().getString())
                                .createdDate(articleBase.getDate().getUnixCreateDate())
                                .updatedDate(articleBase.getDate().getUnixUpdateDate())
                                .build();
    }

    public static ArticleBaseEntity toArticleBaseEntity(
        Map<Object, Object> articleVertex, String status) {
        return ArticleBaseEntity.builder()
                                .id(VertexConverter.toId(articleVertex))
                                .imageUrl(VertexConverter.toString(ArticleVertexProperty.IMAGE_URL.getString(),
                                                                   articleVertex))
                                .title(VertexConverter.toString(ArticleVertexProperty.TITLE.getString(), articleVertex))
                                .status(status)
                                .createdDate(VertexConverter.toLong(ArticleVertexProperty.CREATED_TIME.getString(),
                                                                    articleVertex))
                                .updatedDate(VertexConverter.toLong(ArticleVertexProperty.UPDATED_TIME.getString(),
                                                                    articleVertex))
                                .build();
    }
}
