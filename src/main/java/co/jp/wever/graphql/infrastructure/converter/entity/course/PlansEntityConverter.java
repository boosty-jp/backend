package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.infrastructure.datamodel.course.PlanEntity;

public class PlansEntityConverter {
    public static List<PlanEntity> toPlans(List<Map<Object, Object>> vertexes) {
//        return vertexes.stream().flatMap(v -> Stream.of(PlanDetailEntityConverter.toPlay(v))).collect(Collectors.toList());
        return null;
    }
}