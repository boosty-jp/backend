package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.tag.CreateTagService;
import graphql.schema.DataFetcher;

@Component
public class TagDataFetcher {
    private final CreateTagService createTagService;

    public TagDataFetcher(CreateTagService createTagService) {
        this.createTagService = createTagService;
    }

    public DataFetcher createTagDataFetcher() {

        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String tagId = createTagService.createTag(name);
            //TODO: リクエストヘッダから取得する

            return CreateResponse.builder().id(tagId).build();
        };
    }
}
