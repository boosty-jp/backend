package co.jp.wever.graphql.application.converter.article;

import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleOutlineResponse;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleOutline;

public class ArticleOutlineResponseConverter {
    public static ArticleOutlineResponse toArticleOutlineResponse(ArticleOutline articleOutline) {
        return ArticleOutlineResponse.builder()
                                     .base(ArticleBaseResponseConverter.toArticleBaseResponse(articleOutline.getArticleBase()))
                                     .author(UserResponseConverter.toUserResponse(articleOutline.getAuthor()))
                                     .statistics(ArticleStatisticsResponseConverter.toArticleStatisticsResponse(
                                         articleOutline.getStatistics()))
                                     .build();
    }
}
