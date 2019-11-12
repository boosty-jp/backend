package co.jp.wever.graphql.application.datamodel.response.query.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagStatisticResponse {
    private String id;
    private String name;
    private long related;
}
