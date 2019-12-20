package co.jp.wever.graphql.application.converter.skill;

import co.jp.wever.graphql.application.datamodel.response.query.skill.SkillResponse;
import co.jp.wever.graphql.domain.domainmodel.skill.Skill;
import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;

public class SkillResponseConverter {
    public static SkillResponse toSkillResponse(Skill skill) {
        return SkillResponse.builder()
                            .id(skill.getId().getValue())
                            .name(skill.getName().getValue())
                            .level(skill.getLevel().getValue())
                            .build();
    }

    public static SkillResponse toSkillResponse(SkillEntity skill) {
        return SkillResponse.builder()
                            .id(skill.getId())
                            .name(skill.getName())
                            .level(skill.getLevel())
                            .build();
    }

    public static SkillResponse toSkillResponseWithNoLevel(SkillEntity skill) {
        return SkillResponse.builder()
                            .id(skill.getId())
                            .name(skill.getName())
                            .build();
    }
}
