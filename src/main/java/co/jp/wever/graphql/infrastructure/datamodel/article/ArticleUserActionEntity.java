package co.jp.wever.graphql.infrastructure.datamodel.article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleUserActionEntity {
    private boolean liked;
    private boolean bookmarked;
    private boolean learned;
}
