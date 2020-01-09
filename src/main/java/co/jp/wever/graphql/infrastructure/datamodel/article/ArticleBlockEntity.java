package co.jp.wever.graphql.infrastructure.datamodel.article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBlockEntity {
    private String id;
    private String type;
    private String data;
}
