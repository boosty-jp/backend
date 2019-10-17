package co.jp.wever.graphql.infrastructure.converter.entity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

public class PlansEntityConverter {
    public static List<PlanEntity> toPlans(List<Map<Object, Object>> vertexes) {
        return vertexes.stream().flatMap(v -> Stream.of(PlanEntityConverter.toPlay(v))).collect(Collectors.toList());
    }
}