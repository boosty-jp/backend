package co.jp.wever.graphql.application.datamodel.response.query.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanDetailResponse {
    private PlanBaseResponse base;
    private UserResponse author;
    private List<TagResponse> tags;
    private List<PlanElementResponse> elements;
    private PlanStatisticsResponse statistics;
    private PlanActionResponse action;
}
