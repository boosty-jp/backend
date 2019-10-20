package co.jp.wever.graphql.application.datamodel.response.query.article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleUserActionResponse {
    private boolean liked;
    private boolean learned;
}
