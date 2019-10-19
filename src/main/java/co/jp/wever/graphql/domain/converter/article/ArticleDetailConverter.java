package co.jp.wever.graphql.domain.converter.article;

import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.tag.TagConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserDescription;
import co.jp.wever.graphql.domain.domainmodel.user.UserDisplayName;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.domainmodel.user.UserImageUrl;
import co.jp.wever.graphql.domain.domainmodel.user.UserUrl;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

public class ArticleDetailConverter {
    public static ArticleDetail toArticleDetail(ArticleDetailEntity articleDetailEntity) {
        User author = new User(UserId.of(articleDetailEntity.getAuthor().getUserId()),
                               UserDisplayName.of(articleDetailEntity.getAuthor().getDisplayName()),
                               UserDescription.of(articleDetailEntity.getAuthor().getDescription()),
                               UserImageUrl.of(articleDetailEntity.getAuthor().getImageUrl()),
                               UserUrl.of(articleDetailEntity.getAuthor().getUrl()),
                               articleDetailEntity.getAuthor()
                                                  .getTags()
                                                  .stream()
                                                  .map(t -> TagConverter.toTag(t))
                                                  .collect(Collectors.toList()));

        return new ArticleDetail(ArticleBaseConverter.toArticleBase(articleDetailEntity.getBase()),
                                 articleDetailEntity.getTags()
                                                    .stream()
                                                    .map(t -> Tag.of(t.getTagId(), t.getName()))
                                                    .collect(Collectors.toList()),
                                 author,
                                 ArticleStatisticsConverter.toArticleStatistics(articleDetailEntity.getStatistics()),
                                 ArticleUserActionConverter.toArticleUserAction(articleDetailEntity.getActions()));
    }
}