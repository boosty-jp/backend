package jp.boosty.backend.domain.service.datafetchers;

import jp.boosty.backend.application.converter.tag.TagResponseConverter;
import jp.boosty.backend.domain.service.tag.TagQueryService;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

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
