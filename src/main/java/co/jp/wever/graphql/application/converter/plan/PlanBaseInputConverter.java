package co.jp.wever.graphql.application.converter.plan;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;

public class PlanBaseInputConverter {
    public static PlanBaseInput toPlanBaseInput(Map<String, Object> input) {
        try {
            return PlanBaseInput.builder()
                                .title(input.get("title").toString())
                                .description(input.get("description").toString())
                                .imageUrl(input.get("imageUrl").toString())
                                .tags((List<String>) input.get("tags"))
                                .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
