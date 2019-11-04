package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.section.CreateSectionInputConverter;
import co.jp.wever.graphql.application.converter.section.SectionResponseConverter;
import co.jp.wever.graphql.application.converter.section.UpdateSectionInputConverter;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
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
    private final RequesterConverter requesterConverter;

    public SectionDataFetcher(
        TokenVerifier tokenVerifier,
        FindSectionService findSectionService,
        CreateSectionService createSectionService,
        UpdateSectionService updateSectionService,
        DeleteSectionService deleteSectionService, RequesterConverter requesterConverter) {
        this.tokenVerifier = tokenVerifier;
        this.findSectionService = findSectionService;
        this.createSectionService = createSectionService;
        this.updateSectionService = updateSectionService;
        this.deleteSectionService = deleteSectionService;
        this.requesterConverter = requesterConverter;
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
                                     .sorted(Comparator.comparing(FindSection::getNumber)) //番号順にする
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
                                                                 CreateSectionInputConverter.toCreateSectionInput(sectionInputMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher updateSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {

            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            UpdateSectionInput updateSectionInput = UpdateSectionInputConverter.toUpdateSectionInput(dataFetchingEnvironment);
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            updateSectionService.updateSection(sectionId,
                                               updateSectionInput,
                                               requester);

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

    public DataFetcher deleteLikeSectionElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");

            updateSectionService.deleteLikeSection(sectionId, userId);

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
