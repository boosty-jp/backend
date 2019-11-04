package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.domainmodel.article.base.PublishArticleBase;

public class PublishArticleBaseConverter {
    public static PublishArticleBase toPublishArticleBase(ArticleInput articleInput) {
        return PublishArticleBase.of(articleInput.getId(),
                              articleInput.getTitle(),
                              articleInput.getImageUrl(),
                              articleInput.getTags());
    }
}
