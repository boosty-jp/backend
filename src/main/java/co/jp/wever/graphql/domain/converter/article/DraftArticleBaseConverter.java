package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.domainmodel.article.base.DraftArticleBase;

public class DraftArticleBaseConverter {
    public static DraftArticleBase toDraftArticleBase(ArticleInput articleInput) {
        return DraftArticleBase.of(articleInput.getId(),
                                   articleInput.getTitle(),
                                   articleInput.getImageUrl(),
                                   articleInput.getTags());
    }
}
