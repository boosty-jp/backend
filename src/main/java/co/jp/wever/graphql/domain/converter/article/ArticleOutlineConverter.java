package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleOutline;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleOutlineEntity;

public class ArticleOutlineConverter {
    public static ArticleOutline toArticleOutline(ArticleOutlineEntity articleOutlineEntity) {
        return new ArticleOutline(ArticleBaseConverter.toArticleBase(articleOutlineEntity.getBase()),
                                  ArticleStatisticsConverter.toArticleStatistics(articleOutlineEntity.getStatistics()),
                                  UserConverter.toUser(articleOutlineEntity.getAuthor()));
    }
}
