package co.jp.wever.graphql.domain.repository.skill;

import co.jp.wever.graphql.domain.domainmodel.skill.SkillName;

public interface SkillMutationRepository {
    String createSkill(SkillName name);
}
