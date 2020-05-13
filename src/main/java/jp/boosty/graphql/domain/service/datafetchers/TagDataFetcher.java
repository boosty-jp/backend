package jp.boosty.graphql.domain.service.datafetchers;

import jp.boosty.graphql.application.converter.tag.TagResponseConverter;
import jp.boosty.graphql.domain.service.tag.TagQueryService;
import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import graphql.schema.DataFetcher;

@Component
public class TagDataFetcher {
    private final TagQueryService tagQueryService;

    public TagDataFetcher(
         TagQueryService tagQueryService ) {
        this.tagQueryService = tagQueryService;
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
