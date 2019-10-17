package co.jp.wever.graphql.application.datamodel.response.query;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanBaseResponse {
    private String id;
    private String title;
    private String description;
    private List<TagResponse> tags;
    private String image;
    private String status;
    private UserResponse author;
}
