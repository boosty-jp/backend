package co.jp.wever.graphql.application.converter.article;

import java.util.Date;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.skill.SkillResponseConverter;
import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.AccountActionResponseConverter;
import co.jp.wever.graphql.application.converter.user.ActionCountResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleResponse;
import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToContentProperty;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class ArticleResponseConverter {
    public static ArticleResponse toArticleResponse(Article article) {
        return ArticleResponse.builder()
                              .id(article.getBase().getId())
                              .title(article.getBase().getTitle())
                              .imageUrl(article.getBase().getImageUrl())
                              .status(article.getBase().getStatus().getString())
                              .textUrl(article.getBase().getTextUrl().getValue())
                              .createDate(DateToStringConverter.toDateString(article.getBase()
                                                                                    .getDate()
                                                                                    .getCreateDate()))
                              .updateDate(DateToStringConverter.toDateString(article.getBase()
                                                                                    .getDate()
                                                                                    .getUpdateDate()))
                              .tags(article.getTags()
                                           .stream()
                                           .map(t -> TagResponseConverter.toTagResponse(t))
                                           .collect(Collectors.toList()))
                              .author(UserResponseConverter.toUserResponse(article.getAuthor()))
                              .skills(article.getSkills()
                                             .getSkills()
                                             .stream()
                                             .map(s -> SkillResponseConverter.toSkillResponse(s))
                                             .collect(Collectors.toList()))
                              .accountAction(AccountActionResponseConverter.toAccountAction(article.getAccountAction()))
                              .actionCount(ActionCountResponseConverter.toActionCountResponse(article.getActionCount()))
                              .build();
    }

    public static ArticleResponse toArticleResponseForList(ArticleEntity article) {
        return ArticleResponse.builder()
                              .id(article.getBase().getId())
                              .title(article.getBase().getTitle())
                              .imageUrl(article.getBase().getImageUrl())
                              .status(UserToContentProperty.PUBLISHED.getString())
                              .createDate(DateToStringConverter.toDateString(new Date(article.getBase()
                                                                                             .getCreatedDate())))
                              .updateDate(DateToStringConverter.toDateString(new Date(article.getBase()
                                                                                             .getUpdatedDate())))
                              .tags(article.getTags()
                                           .stream()
                                           .map(t -> TagResponseConverter.toTagResponse(t))
                                           .collect(Collectors.toList()))
                              .skills(article.getSkills()
                                             .stream()
                                             .map(s -> SkillResponseConverter.toSkillResponse(s))
                                             .collect(Collectors.toList()))
                              .actionCount(ActionCountResponseConverter.toActionCountResponse(article.getActionCount()))
                              .build();
    }
}
