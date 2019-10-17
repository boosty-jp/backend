package co.jp.wever.graphql.application.datamodel.response.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponse {
    private String id;
    private String name;
}
