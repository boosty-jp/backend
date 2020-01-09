package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleBlockVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBlockEntity;

public class ArticleBlockEntityConverter {
    public static ArticleBlockEntity toArticleBlockEntity(
        Map<Object, Object> blockVertex) {
        return ArticleBlockEntity.builder()
                                 .id(VertexConverter.toId(blockVertex))
                                 .type(VertexConverter.toString(ArticleBlockVertexProperty.TYPE.getString(),
                                                                blockVertex))
                                 .data(VertexConverter.toString(ArticleBlockVertexProperty.DATA.getString(),
                                                                blockVertex))
                                 .build();
    }
}
