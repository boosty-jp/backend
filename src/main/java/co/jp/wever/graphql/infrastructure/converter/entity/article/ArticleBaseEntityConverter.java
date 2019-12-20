package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseEntityConverter {
    public static ArticleBaseEntity toArticleBaseEntity(
        Map<Object, Object> articleVertex, String status) {
        return ArticleBaseEntity.builder()
                                .id(VertexConverter.toId(articleVertex))
                                .imageUrl(VertexConverter.toString(ArticleVertexProperty.IMAGE_URL.getString(),
                                                                   articleVertex))
                                .title(VertexConverter.toString(ArticleVertexProperty.TITLE.getString(), articleVertex))
                                .status(status)
                                .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(),
                                                                    articleVertex))
                                .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(),
                                                                    articleVertex))
                                .build();
    }
}
