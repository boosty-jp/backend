package co.jp.wever.graphql.infrastructure.datamodel.article.aggregation;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleOutlineEntity {
    private ArticleBaseEntity base;
    private UserEntity author;
    private List<TagEntity> tags;
    private ArticleStatisticsEntity statistics;
}
