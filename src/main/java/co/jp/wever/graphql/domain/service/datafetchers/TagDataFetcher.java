package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.tag.TagMutationService;
import co.jp.wever.graphql.domain.service.tag.TagQueryService;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import graphql.schema.DataFetcher;

@Component
public class TagDataFetcher {
    private final TagMutationService tagMutationService;
    private final TagQueryService tagQueryService;
    private final RequesterConverter requesterConverter;

    public TagDataFetcher(
        TagMutationService tagMutationService, TagQueryService tagQueryService, RequesterConverter requesterConverter) {
        this.tagMutationService = tagMutationService;
        this.tagQueryService = tagQueryService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher createTagDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String name = dataFetchingEnvironment.getArgument("name");
            String tagId = tagMutationService.createTag(name, requester);

            return CreateResponse.builder().id(tagId).build();
        };
    }

    public DataFetcher famousTagDataFetcher() {
        return dataFetchingEnvironment -> {

            List<TagEntity> results = tagQueryService.famousTags();
            return results.stream()
                          .map(r -> TagResponseConverter.toTagResponseWithRelatedCount(r))
                          .collect(Collectors.toList());
        };
    }
}
