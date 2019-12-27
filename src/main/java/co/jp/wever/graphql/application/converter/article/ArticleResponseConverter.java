package co.jp.wever.graphql.application.converter.article;

import java.util.Date;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.skill.SkillResponseConverter;
import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.AccountActionResponseConverter;
import co.jp.wever.graphql.application.converter.user.ActionCountResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleBlockResponse;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleResponse;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class ArticleResponseConverter {
    public static ArticleResponse toArticleResponse(ArticleEntity article) {
        return ArticleResponse.builder()
                              .id(article.getBase().getId())
                              .title(article.getBase().getTitle())
                              .imageUrl(article.getBase().getImageUrl())
                              .status(article.getBase().getStatus())
                              .blocks(article.getBlocks()
                                             .stream()
                                             .map(a -> ArticleBlockResponse.builder()
                                                                           .type(a.getType())
                                                                           .data(a.getData())
                                                                           .build())
                                             .collect(Collectors.toList()))
                              .createDate(DateToStringConverter.toDateString(new Date(article.getBase()
                                                                                             .getCreatedDate())))
                              .updateDate(DateToStringConverter.toDateString(new Date(article.getBase()
                                                                                             .getUpdatedDate())))
                              .tags(article.getTags()
                                           .stream()
                                           .map(t -> TagResponseConverter.toTagResponse(t))
                                           .collect(Collectors.toList()))
                              .author(UserResponseConverter.toUserResponse(article.getAuthor()))
                              .skills(article.getSkills()
                                             .stream()
                                             .map(s -> SkillResponseConverter.toSkillResponse(s))
                                             .collect(Collectors.toList()))
                              .accountAction(AccountActionResponseConverter.toAccountAction(article.getActions()))
                              .actionCount(ActionCountResponseConverter.toActionCountResponse(article.getActionCount()))
                              .build();
    }

    public static ArticleResponse toArticleResponseForList(ArticleEntity article) {
        return ArticleResponse.builder()
                              .id(article.getBase().getId())
                              .title(article.getBase().getTitle())
                              .imageUrl(article.getBase().getImageUrl())
                              .status(article.getBase().getStatus())
//                              .createDate(DateToStringConverter.toDateString(new Date(article.getBase()
//                                                                                             .getCreatedDate())))
//                              .updateDate(DateToStringConverter.toDateString(new Date(article.getBase()
//                                                                                             .getUpdatedDate())))
                              .createDate(String.valueOf(article.getBase().getCreatedDate() ))
                              .updateDate(String.valueOf(article.getBase().getUpdatedDate()))
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

    public static ArticleResponse toArticleResponseForPublishedList(ArticleEntity article) {
        return ArticleResponse.builder()
                              .id(article.getBase().getId())
                              .title(article.getBase().getTitle())
                              .imageUrl(article.getBase().getImageUrl())
                              .status(EdgeLabel.PUBLISH.getString())
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
