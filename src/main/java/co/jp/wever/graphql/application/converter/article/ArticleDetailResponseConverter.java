package co.jp.wever.graphql.application.converter.article;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleDetailResponse;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;

public class ArticleDetailResponseConverter {
    public static ArticleDetailResponse toArticleDetailResponse(ArticleDetail articleDetail) {
        return ArticleDetailResponse.builder()
                                    .base(ArticleBaseResponseConverter.toArticleBaseResponse(articleDetail.getBase()))
                                    .action(ArticleUserActionResponseConverter.toArticleUserActionResponse(articleDetail
                                                                                                               .getUserAction()))
                                    .author(UserResponseConverter.toUserResponse(articleDetail.getAuthor()))
                                    .statistics(ArticleStatisticsResponseConverter.toArticleStatisticsResponse(
                                        articleDetail.getStatistics()))
                                    .tags(articleDetail.getTags()
                                                       .stream()
                                                       .map(t -> TagResponseConverter.toTagResponse(t))
                                                       .collect(Collectors.toList()))
                                    .build();
    }
}
