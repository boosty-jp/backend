package co.jp.wever.graphql.infrastructure.converter.entity;

import org.springframework.stereotype.Component;

import java.util.Map;


import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Component
public class PlanEntityConverter {
//    public static PlanEntity toPlan(
//        Map<Object, Object> planVertex,
//        Map<Object, Object> tagVertex,
//        Map<Object, Object> userVertex,
//        Map<Object, Object> statusVertex,
//        Map<Object, Object> elementsVertex) {
//
//        return PlanEntity.builder()
//                         .id(VertexConverter.toId(planVertex))
//                         .baseEntity(PlanBaseEntityConverter.toPlanBaseEntity(planVertex,
//                                                                              tagVertex,
//                                                                              userVertex,
//                                                                              statusVertex))
//                         .elementEntities(null)
//                         .build();
//    }

    //TODO: Neptuneの動きを確認してから実装
    public static PlanEntity toPlay(Map<Object, Object> planVertex){
        return PlanEntity.builder().build();
    }
}
