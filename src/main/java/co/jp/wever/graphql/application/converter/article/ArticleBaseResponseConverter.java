package co.jp.wever.graphql.application.converter.article;

import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class ArticleBaseResponseConverter {
    public static ArticleBaseResponse toArticleBaseResponse(ArticleBase articleBase) {
        return ArticleBaseResponse.builder()
                                  .id(articleBase.getId())
                                  .title(articleBase.getTitle())
                                  .imageUrl(articleBase.getImageUrl())
                                  .status(articleBase.getStatus().getString())
                                  .createDate(DateToStringConverter.toDateString(articleBase.getDate().getCreateDate()))
                                  .updateDate(DateToStringConverter.toDateString(articleBase.getDate().getUpdateDate()))
                                  .build();
    }
}
