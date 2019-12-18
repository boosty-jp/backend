package co.jp.wever.graphql.infrastructure.converter.entity.skill;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;

public class SkillEntityConverter {
    public static SkillEntity toSkillEntity(Map<Object, Object> skillVertex, Map<Object, Object> skillEdge) {
        return SkillEntity.builder()
                          .id(VertexConverter.toId(skillVertex))
                          .name(VertexConverter.toString("name", skillVertex))
                          .level(VertexConverter.toLevel(skillEdge))
                          .teachCount(0)
                          .build();
    }
}
