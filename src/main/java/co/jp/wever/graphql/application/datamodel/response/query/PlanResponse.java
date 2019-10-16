package co.jp.wever.graphql.application.datamodel.response.query;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanResponse {
    private String id;
    private String title;
    private String description;
    private List<ElementResponse> elementResponses;
    private String image;
}
