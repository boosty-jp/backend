package co.jp.wever.graphql.domain.service.skill;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;
import co.jp.wever.graphql.infrastructure.repository.skill.SkillQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SkillQueryService {
    private final SkillQueryRepositoryImpl skillQueryRepository;

    public SkillQueryService(SkillQueryRepositoryImpl skillQueryRepository) {
        this.skillQueryRepository = skillQueryRepository;
    }

    public List<SkillEntity> famousSkill() {
        return skillQueryRepository.famousSkill();
    }
}
