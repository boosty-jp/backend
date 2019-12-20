package co.jp.wever.graphql.application.datamodel.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBlockInput {
    private String type;
    private String data;
}
