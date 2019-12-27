package co.jp.wever.graphql.infrastructure.datamodel.article;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleListEntity {
    private List<ArticleEntity> articles;
    private long sumCount;
}
