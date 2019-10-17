package co.jp.wever.graphql.application.datamodel.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanBaseInput {
    private String title;
    private String description;
    private List<String> tags;
    private String imageUrl;
}
