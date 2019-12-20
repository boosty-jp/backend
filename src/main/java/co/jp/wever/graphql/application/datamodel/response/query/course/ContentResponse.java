package co.jp.wever.graphql.application.datamodel.response.query.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentResponse {
    private String id;
    private String title;
    private long number;
    private boolean learned;
}
