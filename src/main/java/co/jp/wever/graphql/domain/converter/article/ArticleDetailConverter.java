package co.jp.wever.graphql.domain.converter.article;

import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

public class ArticleDetailConverter {
    public static ArticleDetail toArticleDetail(ArticleDetailEntity articleDetailEntity) {
        return new ArticleDetail(ArticleBaseConverter.toArticleBase(articleDetailEntity.getBase()),
                                 articleDetailEntity.getTags()
                                                    .stream()
                                                    .map(t -> Tag.of(t.getTagId(), t.getName()))
                                                    .collect(Collectors.toList()),
                                 User.of(articleDetailEntity.getAuthor().getUserId()),
                                 ArticleStatisticsConverter.toArticleStatistics(articleDetailEntity.getStatistics()),
                                 ArticleUserActionConverter.toArticleUserAction(articleDetailEntity.getActions()));
    }
}