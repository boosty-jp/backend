package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import co.jp.wever.graphql.infrastructure.datamodel.SectionEntity;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class SectionDataFetcher {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    public DataFetcher sectionDataFetcher() {
        return dataFetchingEnvironment -> {
            return SectionEntity.builder().id("").build();
        };
    }

    public DataFetcher allLikedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<SectionEntity> sectionEntities = new ArrayList<>();
            return sectionEntities;
        };
    }

    public DataFetcher allBookmarkedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<SectionEntity> sectionEntities = new ArrayList<>();
            return sectionEntities;
        };
    }

    public DataFetcher famousSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<SectionEntity> sectionEntities = new ArrayList<>();
            return sectionEntities;
        };
    }

    public DataFetcher relatedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<SectionEntity> sectionEntities = new ArrayList<>();
            return sectionEntities;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher addSectionDataFetcher() {
        return dataFetchingEnvironment -> CreateResponse.builder()
                                                        .id("1")
                                                        .error(ErrorResponse.builder()
                                                                            .errorCode("code")
                                                                            .errorMessage("error")
                                                                            .build());
    }

    public DataFetcher updateSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder()
                                                        .error(ErrorResponse.builder()
                                                                            .errorCode("code")
                                                                            .errorMessage("error")
                                                                            .build());
    }

    public DataFetcher bookmarkSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder()
                                                        .error(ErrorResponse.builder()
                                                                            .errorCode("code")
                                                                            .errorMessage("error")
                                                                            .build());
    }

    public DataFetcher likeSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder()
                                                        .error(ErrorResponse.builder()
                                                                            .errorCode("code")
                                                                            .errorMessage("error")
                                                                            .build());
    }
}
