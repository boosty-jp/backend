package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import co.jp.wever.graphql.application.datamodel.CreateResponse;
import co.jp.wever.graphql.application.datamodel.ErrorResponse;
import co.jp.wever.graphql.infrastructure.datamodel.Section;
import co.jp.wever.graphql.application.datamodel.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class SectionDataFetcher {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    public DataFetcher sectionDataFetcher() {
        return dataFetchingEnvironment -> {
            return Section.builder().id(1L).build();
        };
    }

    public DataFetcher allLikedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Section> sections = new ArrayList<>();
            return sections;
        };
    }

    public DataFetcher allBookmarkedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Section> sections = new ArrayList<>();
            return sections;
        };
    }

    public DataFetcher famousSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Section> sections = new ArrayList<>();
            return sections;
        };
    }

    public DataFetcher relatedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Section> sections = new ArrayList<>();
            return sections;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher addSectionDataFetcher() {
        return dataFetchingEnvironment -> CreateResponse.builder().id("1").error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher updateSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher bookmarkSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher likeSectionElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
