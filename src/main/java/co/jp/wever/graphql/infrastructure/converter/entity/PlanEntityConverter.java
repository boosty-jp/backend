package co.jp.wever.graphql.infrastructure.converter.entity;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Component
public class PlanEntityConverter {
    public static PlanEntity toPlan(Map<Object, Object> vertex) {
        return PlanEntity.builder().id(VertexConverter.toId(vertex)).title(VertexConverter.toString("title", vertex)).image(VertexConverter.toString("image", vertex)).description(VertexConverter.toString(
            "description",
            vertex)).build();
    }
}
