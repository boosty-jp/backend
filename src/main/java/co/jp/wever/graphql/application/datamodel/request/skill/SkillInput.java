package co.jp.wever.graphql.application.datamodel.request.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillInput {
    private String id;
    private int level;
}
