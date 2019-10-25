package co.jp.wever.graphql.application.converter.article;

import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleBaseResponse;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;

public class ArticleBaseResponseConverter {
    public static ArticleBaseResponse toArticleBaseResponse(ArticleBase articleBase) {
        return ArticleBaseResponse.builder()
                                  .id(articleBase.getId())
                                  .title(articleBase.getTitle())
                                  .imageUrl(articleBase.getImageUrl())
                                  .status(articleBase.getStatus().getString())
                                  .createDate(articleBase.getDate().getCreateDate().toString())
                                  .updateDate(articleBase.getDate().getUpdateDate().toString())
                                  .build();
    }
}
