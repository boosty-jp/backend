package co.jp.wever.graphql.application.datamodel.response.query.plan;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanResponse {
    private PlanBaseResponse baseResponse;
    private List<ElementResponse> elementResponses;
}
