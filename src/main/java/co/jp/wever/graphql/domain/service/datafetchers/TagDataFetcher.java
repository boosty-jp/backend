package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateTagResponse;
import co.jp.wever.graphql.domain.service.tag.CreateTagService;
import co.jp.wever.graphql.domain.service.tag.FindTagService;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import graphql.schema.DataFetcher;

@Component
public class TagDataFetcher {
    private final CreateTagService createTagService;
    private final FindTagService findTagService;
    private final RequesterConverter requesterConverter;

    public TagDataFetcher(
        CreateTagService createTagService, FindTagService findTagService, RequesterConverter requesterConverter) {
        this.createTagService = createTagService;
        this.findTagService = findTagService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher createTagDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String name = dataFetchingEnvironment.getArgument("name");
            String tagId = createTagService.createTag(name, requester);

            return CreateTagResponse.builder().id(tagId).name(name).build();
        };
    }

    public DataFetcher famousTagDataFetcher() {
        return dataFetchingEnvironment -> {

            List<TagEntity> results = findTagService.famousTags();
            return results.stream()
                          .map(r -> TagResponseConverter.toTagResponseWithRelatedCount(r))
                          .collect(Collectors.toList());
        };
    }
}
