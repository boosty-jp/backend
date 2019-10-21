package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

public class ArticleDetailEntityConverter {
    public static ArticleDetailEntity toArticleDetailEntity(
        Map<Object, Object> articleVertex,
        List<Map<Object, Object>> tagVertexs,
        Map<Object, Object> authorVertex,
        long like,
        long learned) {
        return ArticleDetailEntity.builder()
                                  .base(ArticleBaseEntityConverter.toArticleBaseEntity(articleVertex, "status"))
                                  .tags(tagVertexs.stream()
                                                  .map(t -> TagEntityConverter.toTagEntity(t))
                                                  .collect(Collectors.toList()))
                                  .author(UserEntityConverter.toUserEntity(authorVertex))
                                  .statistics(ArticleStatisticsEntity.builder()
                                                                     .likeCount(like)
                                                                     .learnedCount(learned)
                                                                     .build())
                                  .build();
    }
}
