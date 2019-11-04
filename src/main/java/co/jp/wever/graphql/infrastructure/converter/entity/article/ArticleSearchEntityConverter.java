package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;

public class ArticleSearchEntityConverter {
    public static ArticleSearchEntity toArticleSearchEntity(
        Map<String, Object> result, String authorId, String articleId, long publishTime) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) result.get("tags");
        long like = (long) result.get("liked");
        long learned = (long) result.get("learned");

        return ArticleSearchEntity.builder()
                                  .objectID(articleId)
                                  .imageUrl(VertexConverter.toString(ArticleVertexProperty.IMAGE_URL.getString(),
                                                                     baseResult))
                                  .authorId(authorId)
                                  .learned(learned)
                                  .like(like)
                                  .publishDate(publishTime)
                                  .tags(tagResult.stream()
                                                 .map(t -> VertexConverter.toString("name", t))
                                                 .collect(Collectors.toList()))
                                  .updateDate(VertexConverter.toLong(ArticleVertexProperty.UPDATED_TIME.getString(),
                                                                     baseResult))
                                  .title(VertexConverter.toString(ArticleVertexProperty.TITLE.getString(), baseResult))
                                  .build();
    }

    public static ArticleSearchEntity toArticleSearchEntity(PublishArticle article, long updateTime, String authorId) {
        return ArticleSearchEntity.builder()
                                  .objectID(article.getId())
                                  .title(article.getTitle())
                                  .tags(article.getTagIds())
                                  .imageUrl(article.getImageUrl())
                                  .like(0)
                                  .learned(0)
                                  .authorId(authorId)
                                  .updateDate(updateTime)
                                  .publishDate(updateTime)
                                  .build();
    }
}
