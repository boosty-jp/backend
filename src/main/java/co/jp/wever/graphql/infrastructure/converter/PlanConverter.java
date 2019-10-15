package co.jp.wever.graphql.infrastructure.converter;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.Plan;

@Component
public class PlanConverter {
    public static Plan toPlan(Map<Object, Object> vertex) {
        return Plan.builder().id(VertexConverter.toId(vertex)).title(VertexConverter.toString("title", vertex)).image(VertexConverter.toString("image", vertex)).description(VertexConverter.toString(
            "description",
            vertex)).build();
    }
}
