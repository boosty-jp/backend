package co.jp.wever.graphql.application.converter.article;

import co.jp.wever.graphql.domain.domainmodel.article.action.ArticleUserAction;

public class ArticleUserActionResponseConverter {
    public static ArticleUserActionResponse toArticleUserActionResponse(ArticleUserAction articleUserAction) {
        return ArticleUserActionResponse.builder()
                                        .liked(articleUserAction.isLiked())
                                        .learned(articleUserAction.isLearned())
                                        .build();
    }
}
