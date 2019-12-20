package co.jp.wever.graphql.domain.service.skill;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.skill.SkillMutationRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SkillMutationService {
    private final SkillMutationRepositoryImpl skillMutationRepository;

    public SkillMutationService(SkillMutationRepositoryImpl skillMutationRepository) {
        this.skillMutationRepository = skillMutationRepository;
    }

    public String createSkill(String name, Requester requester) {
        log.info("create tag name: {}", name);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return skillMutationRepository.createSkill(name);
    }
}
