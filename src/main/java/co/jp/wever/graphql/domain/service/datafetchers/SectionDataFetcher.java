package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.section.SectionInputConverter;
import co.jp.wever.graphql.application.converter.section.SectionResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import co.jp.wever.graphql.domain.service.section.CreateSectionService;
import co.jp.wever.graphql.domain.service.section.DeleteSectionService;
import co.jp.wever.graphql.domain.service.section.FindSectionService;
import co.jp.wever.graphql.domain.service.section.UpdateSectionService;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class SectionDataFetcher {

    private final TokenVerifier tokenVerifier;
    private final FindSectionService findSectionService;
    private final CreateSectionService createSectionService;
    private final UpdateSectionService updateSectionService;
    private final DeleteSectionService deleteSectionService;

    public SectionDataFetcher(
        TokenVerifier tokenVerifier,
        FindSectionService findSectionService,
        CreateSectionService createSectionService,
        UpdateSectionService updateSectionService,
        DeleteSectionService deleteSectionService) {
        this.tokenVerifier = tokenVerifier;
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
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");

            return findSectionService.findAllSectionsOnArticle(articleId, userId)
                                     .stream()
                                     .map(s -> SectionResponseConverter.toSectionResponse(s))
                                     .collect(Collectors.toList());
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
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            return findSectionService.findRelatedSections(userId);
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher createSectionDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            Map<String, Object> sectionInputMap = (Map) dataFetchingEnvironment.getArgument("section");
            String articleId = dataFetchingEnvironment.getArgument("articleId");

            String createId = createSectionService.createSection(userId,
                                                                 articleId,
                                                                 SectionInputConverter.toSectionInput(sectionInputMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher updateSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");

            Map<String, Object> sectionInputMap = (Map) dataFetchingEnvironment.getArgument("section");

            updateSectionService.updateSection(sectionId,
                                               SectionInputConverter.toSectionInput(sectionInputMap),
                                               userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher likeSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");

            updateSectionService.likeSection(sectionId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String articleId = dataFetchingEnvironment.getArgument("articleId");

            deleteSectionService.deleteSection(articleId, sectionId, userId);

            return UpdateResponse.builder().build();
        };
    }
}
