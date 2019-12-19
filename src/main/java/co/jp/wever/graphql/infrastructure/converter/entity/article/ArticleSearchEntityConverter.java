package co.jp.wever.graphql.infrastructure.converter.entity.article;

import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;

public class ArticleSearchEntityConverter {
    public static ArticleSearchEntity toArticleSearchEntity(String articleId, Article article, long updateTime) {
        return ArticleSearchEntity.builder()
                                  .objectID(articleId)
                                  .title(article.getBase().getTitle().getValue())
                                  .imageUrl(article.getBase().getImageUrl().getValue())
                                  .like(0)
                                  .learned(0)
                                  .updateDate(updateTime)
                                  .publishDate(updateTime)
                                  .build();
    }
}
