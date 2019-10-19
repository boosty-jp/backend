package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.application.converter.section.SectionInputConverter;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.section.CreateSectionService;
import co.jp.wever.graphql.domain.service.section.DeleteSectionService;
import co.jp.wever.graphql.domain.service.section.FindSectionService;
import co.jp.wever.graphql.domain.service.section.UpdateSectionService;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class SectionDataFetcher {

    private final FindSectionService findSectionService;
    private final CreateSectionService createSectionService;
    private final UpdateSectionService updateSectionService;
    private final DeleteSectionService deleteSectionService;

    public SectionDataFetcher(
        FindSectionService findSectionService,
        CreateSectionService createSectionService,
        UpdateSectionService updateSectionService,
        DeleteSectionService deleteSectionService) {
        this.findSectionService = findSectionService;
        this.createSectionService = createSectionService;
        this.updateSectionService = updateSectionService;
        this.deleteSectionService = deleteSectionService;
    }

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    public DataFetcher allSectionsOnArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findSectionService.findAllSectionsOnArticle(articleId, userId);
        };
    }

    public DataFetcher allLikedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findSectionService.findAllLikedSections(userId);
        };
    }

    public DataFetcher allBookmarkedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findSectionService.findAllBookmarkedSections(userId);
        };
    }

    public DataFetcher famousSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            return findSectionService.findFamousSections();
        };
    }

    public DataFetcher relatedSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findSectionService.findRelatedSections(userId);
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher createSectionDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            Map<String, Object> sectionInputMap = (Map) dataFetchingEnvironment.getArgument("sectionInput");

            String createId =
                createSectionService.createSection(userId, SectionInputConverter.toSectionInput(sectionInputMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher updateSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String userId = dataFetchingEnvironment.getArgument("userId");
            Map<String, Object> sectionInputMap = (Map) dataFetchingEnvironment.getArgument("sectionInput");

            updateSectionService.updateSection(sectionId,
                                               SectionInputConverter.toSectionInput(sectionInputMap),
                                               userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher bookmarkSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String userId = dataFetchingEnvironment.getArgument("userId");

            updateSectionService.bookmarkSection(sectionId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher likeSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String userId = dataFetchingEnvironment.getArgument("userId");

            updateSectionService.likeSection(sectionId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String userId = dataFetchingEnvironment.getArgument("userId");

            deleteSectionService.deleteSection(sectionId, userId);

            return UpdateResponse.builder().build();
        };
    }
}
