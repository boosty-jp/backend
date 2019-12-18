package co.jp.wever.graphql.infrastructure.datamodel.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillEntity {
    private String id;
    private String name;
    private long level;
    private long teachCount;
}
