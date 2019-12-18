package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToContentProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.skill.SkillEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.AccountActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.ActionCountEntity;

public class ArticleEntityConverter {

    public static ArticleEntity toArticleEntity(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) findResult.get("tags");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) findResult.get("skills");
        Map<Object, Object> authorResult = (Map<Object, Object>) findResult.get("author");
        String statusResult = findResult.get("status").toString();

        boolean userLiked = (boolean) findResult.get("userLiked");
        boolean userLearned = (boolean) findResult.get("userLearned");
        long like = (long) findResult.get("liked");
        long learned = (long) findResult.get("learned");

        return ArticleEntity.builder()
                            .base(ArticleBaseEntityConverter.toArticleBaseEntity(baseResult, statusResult))
                            .tags(tagResult.stream()
                                           .map(t -> TagEntityConverter.toTagEntity(t))
                                           .collect(Collectors.toList()))
                            .skills(skillResult.stream()
                                               .map(s -> SkillEntityConverter.toSkillEntity((Map<Object, Object>) s.get(
                                                   "skillVertex"), (Map<Object, Object>) s.get("skillEdge")))
                                               .collect(Collectors.toList()))
                            .author(UserEntityConverter.toUserEntity(authorResult))
                            .actions(AccountActionEntity.builder().learned(userLearned).liked(userLiked).build())
                            .actionCount(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                            .build();
    }

    public static ArticleEntity toArticleEntityForGuest(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) findResult.get("tags");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) findResult.get("skills");
        Map<Object, Object> authorResult = (Map<Object, Object>) findResult.get("author");
        String statusResult = findResult.get("status").toString();

        long like = (long) findResult.get("liked");
        long learned = (long) findResult.get("learned");

        return ArticleEntity.builder()
                            .base(ArticleBaseEntityConverter.toArticleBaseEntity(baseResult, statusResult))
                            .tags(tagResult.stream()
                                           .map(t -> TagEntityConverter.toTagEntity(t))
                                           .collect(Collectors.toList()))
                            .skills(skillResult.stream()
                                               .map(s -> SkillEntityConverter.toSkillEntity((Map<Object, Object>) s.get(
                                                   "skillVertex"), (Map<Object, Object>) s.get("skillEdge")))
                                               .collect(Collectors.toList()))
                            .author(UserEntityConverter.toUserEntity(authorResult))
                            .actions(AccountActionEntity.builder().learned(false).liked(false).build())
                            .actionCount(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                            .build();
    }

    public static ArticleEntity toArticleEntityForList(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) findResult.get("tags");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) findResult.get("skills");

        long like = (long) findResult.get("liked");
        long learned = (long) findResult.get("learned");

        return ArticleEntity.builder()
                            .base(ArticleBaseEntityConverter.toArticleBaseEntity(baseResult,
                                                                                 UserToContentProperty.PUBLISHED.getString()))
                            .tags(tagResult.stream()
                                           .map(t -> TagEntityConverter.toTagEntity(t))
                                           .collect(Collectors.toList()))
                            .skills(skillResult.stream()
                                               .map(s -> SkillEntityConverter.toSkillEntity((Map<Object, Object>) s.get(
                                                   "skillVertex"), (Map<Object, Object>) s.get("skillEdge")))
                                               .collect(Collectors.toList()))
                            .actionCount(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                            .build();
    }

    public static ArticleEntity toArticleEntityForListBySelf(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) findResult.get("tags");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) findResult.get("skills");

        String status = (String) findResult.get("status");
        long like = (long) findResult.get("liked");
        long learned = (long) findResult.get("learned");

        return ArticleEntity.builder()
                            .base(ArticleBaseEntityConverter.toArticleBaseEntity(baseResult, status))
                            .tags(tagResult.stream()
                                           .map(t -> TagEntityConverter.toTagEntity(t))
                                           .collect(Collectors.toList()))
                            .skills(skillResult.stream()
                                               .map(s -> SkillEntityConverter.toSkillEntity((Map<Object, Object>) s.get(
                                                   "skillVertex"), (Map<Object, Object>) s.get("skillEdge")))
                                               .collect(Collectors.toList()))
                            .actionCount(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                            .build();
    }
}
