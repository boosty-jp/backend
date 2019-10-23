package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.article.ArticleUserActionConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

public class ArticleDetailEntityConverter {
    public static ArticleDetailEntity toArticleDetailEntity(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) findResult.get("tags");
        Map<String, Object> authorResult = (Map<String, Object>) findResult.get("author");
        Map<Object, Object> authorBaseResult = (Map<Object, Object>) authorResult.get("base");
        List<Map<Object, Object>> authorTagResult = (List<Map<Object, Object>>) authorResult.get("tags");
        List<String> actionResult = (List<String>) findResult.get("action");
        String statusResult = findResult.get("status").toString();
        long like = (long) findResult.get("liked");
        long learned = (long) findResult.get("learned");

        return ArticleDetailEntity.builder()
                                  .base(ArticleBaseEntityConverter.toArticleBaseEntity(baseResult, statusResult))
                                  .tags(tagResult.stream()
                                                 .map(t -> TagEntityConverter.toTagEntity(t))
                                                 .collect(Collectors.toList()))
                                  .author(UserEntityConverter.toUserEntity(authorBaseResult, authorTagResult))
                                  .actions(ArticleUserActionEntityConverter.toArticleUserActionEntity(actionResult))
                                  .statistics(ArticleStatisticsEntity.builder()
                                                                     .likeCount(like)
                                                                     .learnedCount(learned)
                                                                     .build())
                                  .build();
    }
}
