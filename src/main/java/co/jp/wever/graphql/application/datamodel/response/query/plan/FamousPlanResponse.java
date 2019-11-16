package co.jp.wever.graphql.application.datamodel.response.query.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamousPlanResponse {
    private PlanBaseResponse base;
    private List<TagResponse> tags;
    private UserResponse author;
    private long elementCount;
    private PlanStatisticsResponse statistics;
}
