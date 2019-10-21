package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElementResponse {
    private String id;
    private String title;
    private String type;
    private int number;
}
