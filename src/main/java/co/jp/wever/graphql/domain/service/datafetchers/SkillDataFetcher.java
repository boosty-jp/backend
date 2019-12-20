package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.skill.SkillResponseConverter;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.skill.SkillMutationService;
import co.jp.wever.graphql.domain.service.skill.SkillQueryService;
import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;
import graphql.schema.DataFetcher;

@Component
public class SkillDataFetcher {
    private final SkillMutationService skillMutationService;
    private final SkillQueryService skillQueryService;
    private final RequesterConverter requesterConverter;

    public SkillDataFetcher(
        SkillMutationService skillMutationService,
        SkillQueryService skillQueryService,
        RequesterConverter requesterConverter) {
        this.skillMutationService = skillMutationService;
        this.skillQueryService = skillQueryService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher createSkillDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String name = dataFetchingEnvironment.getArgument("name");
            String skillId = skillMutationService.createSkill(name, requester);

            return CreateResponse.builder().id(skillId).build();
        };
    }

    public DataFetcher famousSkillDataFetcher() {
        return dataFetchingEnvironment -> {

            List<SkillEntity> results = skillQueryService.famousSkill();
            return results.stream()
                          .map(r -> (SkillResponseConverter.toSkillResponseWithNoLevel(r)))
                          .collect(Collectors.toList());
        };
    }
}
