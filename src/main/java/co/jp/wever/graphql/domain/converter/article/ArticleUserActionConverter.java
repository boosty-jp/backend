package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.domainmodel.article.action.ArticleUserAction;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleUserActionEntity;

public class ArticleUserActionConverter {
    public static ArticleUserAction toArticleUserAction(ArticleUserActionEntity articleUserActionEntity) {
        return new ArticleUserAction(articleUserActionEntity.isLiked(),
                                     articleUserActionEntity.isLearned());
    }
}
