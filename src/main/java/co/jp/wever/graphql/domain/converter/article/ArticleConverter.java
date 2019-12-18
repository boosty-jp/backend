package co.jp.wever.graphql.domain.converter.article;

import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;

public class ArticleConverter {
    public static Article toArticle(ArticleEntity articleEntity) {
        User author = UserConverter.toUser(articleEntity.getAuthor());

        return new Article(ArticleBaseConverter.toArticleBase(articleEntity.getBase()),
                           articleEntity.getTags()
                                        .stream()
                                        .map(t -> Tag.of(t.getId(), t.getName()))
                                        .collect(Collectors.toList()),
                           author,
                           ArticleStatisticsConverter.toArticleStatistics(articleEntity.getStatistics()),
                           ArticleUserActionConverter.toArticleUserAction(articleEntity.getActions()));
    }
}
