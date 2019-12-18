package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.domainmodel.action.AccountAction;

public class ArticleUserActionConverter {
    public static AccountAction toArticleUserAction(ArticleUserActionEntity articleUserActionEntity) {
        return new AccountAction(articleUserActionEntity.isLiked(),
                                 articleUserActionEntity.isLearned());
    }
}
