package co.jp.wever.graphql.application.datamodel.response.query.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.TagResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanListItemResponse {
    private PlanBaseResponse base;
    private List<TagResponse> tags;
    private PlanStatisticsResponse statistics;
}
