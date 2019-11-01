package co.jp.wever.graphql.domain.converter.article;

import java.util.Date;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleDate;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleId;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
import co.jp.wever.graphql.domain.domainmodel.article.base.Articletitle;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseConverter {
    public static ArticleBase toArticleBase(ArticleBaseEntity articleBaseEntity) {
        return new ArticleBase(ArticleId.of(articleBaseEntity.getId()),
                               Articletitle.of(articleBaseEntity.getTitle()),
                               ArticleImageUrl.of(articleBaseEntity.getImageUrl()),
                               ArticleStatus.fromString(articleBaseEntity.getStatus()),
                               ArticleDate.of(new Date(articleBaseEntity.getCreatedDate()),
                                              new Date(articleBaseEntity.getUpdatedDate())));
    }

    public static ArticleBase toArticleBase(ArticleInput articleInput) {
        Date now = new Date();
        return new ArticleBase(ArticleId.of(""),
                               Articletitle.of(articleInput.getTitle()),
                               ArticleImageUrl.of(articleInput.getImageUrl()),
                               ArticleStatus.DRAFTED,
                               ArticleDate.of(now, now));
    }
}
