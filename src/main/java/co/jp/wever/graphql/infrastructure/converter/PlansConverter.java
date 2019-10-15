package co.jp.wever.graphql.infrastructure.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.jp.wever.graphql.infrastructure.datamodel.Plan;

public class PlansConverter {
    public static List<Plan> toPlans(List<Map<Object, Object>> vertexes) {
        return vertexes.stream().flatMap(v -> Stream.of(PlanConverter.toPlan(v))).collect(Collectors.toList());
    }
}
