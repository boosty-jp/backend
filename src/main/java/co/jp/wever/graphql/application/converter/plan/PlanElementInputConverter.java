package co.jp.wever.graphql.application.converter.plan;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;

public class PlanElementInputConverter {
    public static PlanElementInput toPlanElement(Map<String, Object> input) {
        try {
            return PlanElementInput.builder()
                                   .number((int) input.get("number"))
                                   .title(input.get("title").toString())
                                   .targetId(input.get("targetId").toString())
                                   .type(input.get("type").toString())
                                   .build();
        } catch (Exception e) {
            //TODO: Exception実装
            return null;
        }
    }
}
