package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleDate;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleDescription;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleId;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
import co.jp.wever.graphql.domain.domainmodel.article.base.Articletitle;
import co.jp.wever.graphql.infrastructure.datamodel.Article;
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
}
