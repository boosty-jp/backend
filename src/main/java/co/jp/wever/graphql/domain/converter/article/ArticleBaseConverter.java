package co.jp.wever.graphql.domain.converter.article;

import java.util.Date;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleDate;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleDescription;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleId;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
import co.jp.wever.graphql.domain.domainmodel.article.base.Articletitle;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

public class ArticleBaseConverter {
    public static ArticleBase toArticleBase(ArticleBaseEntity articleBaseEntity) {
        return new ArticleBase(ArticleId.of(articleBaseEntity.getId()),
                               Articletitle.of(articleBaseEntity.getTitle()),
                               ArticleDescription.of(articleBaseEntity.getDescription()),
                               ArticleImageUrl.of(articleBaseEntity.getImageUrl()),
                               ArticleStatus.valueOf(articleBaseEntity.getStatus()),
                               ArticleDate.of(articleBaseEntity.getCreatedDate(), articleBaseEntity.getUpdatedDate()));
    }

    public static ArticleBase toArticleBase(ArticleInput articleInput) {
        Date now = new Date();
        return new ArticleBase(ArticleId.of(""),
                               Articletitle.of(articleInput.getTitle()),
                               ArticleDescription.of(articleInput.getDescription()),
                               ArticleImageUrl.of(articleInput.getImageUrl()),
                               ArticleStatus.valueOf(ArticleStatus.DRAFTED.name()),
                               ArticleDate.of(now, now));
    }
}
