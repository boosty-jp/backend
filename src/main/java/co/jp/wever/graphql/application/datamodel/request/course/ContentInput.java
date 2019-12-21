package co.jp.wever.graphql.application.datamodel.request.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentInput {
    private String articleId;
    private int number;
}
