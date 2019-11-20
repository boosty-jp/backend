package co.jp.wever.graphql.domain.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.service.datafetchers.ArticleDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.PlanDataFetchers;
import co.jp.wever.graphql.domain.service.datafetchers.SectionDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.TagDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.UserDataFetcher;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {

    private GraphQL graphQL;

    @Autowired
    private PlanDataFetchers planDataFetchers;

    @Autowired
    private ArticleDataFetcher articleDataFetcher;

    @Autowired
    private SectionDataFetcher sectionDataFetcher;

    @Autowired
    private UserDataFetcher userDataFetcher;

    @Autowired
    private TagDataFetcher tagDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        // getFileはだとDeamon実行時のjavaコマンドで使えないのであえてInputStreamを使う
        InputStream is = new ClassPathResource("schema.graphql").getInputStream();
        File file = File.createTempFile("tempSchema", ".graphql");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(file);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                            .type("Query",
                                  typeWiring -> typeWiring.dataFetcher("allSectionOnArticle",
                                                                       sectionDataFetcher.allSectionsOnArticleDataFetcher())
                                                          .dataFetcher("allLikedSections",
                                                                       sectionDataFetcher.allLikedSectionsDataFetcher())
                                                          .dataFetcher("allBookmarkedSections",
                                                                       sectionDataFetcher.allBookmarkedSectionsDataFetcher())
                                                          .dataFetcher("famousSections",
                                                                       sectionDataFetcher.famousSectionsDataFetcher())
                                                          .dataFetcher("article",
                                                                       articleDataFetcher.articleDataFetcher())
                                                          .dataFetcher("allArticles",
                                                                       articleDataFetcher.allArticlesDataFetcher())
                                                          .dataFetcher("allPublishedArticles",
                                                                       articleDataFetcher.allPublishedArticlesDataFetcher())
                                                          .dataFetcher("allDraftedArticles",
                                                                       articleDataFetcher.allDraftedArticlesDataFetcher())
                                                          .dataFetcher("allLikedArticles",
                                                                       articleDataFetcher.allLikedArticlesDataFetcher())
                                                          .dataFetcher("allLearnedArticles",
                                                                       articleDataFetcher.allLearnedArticlesDataFetcher())
                                                          .dataFetcher("famousArticles",
                                                                       articleDataFetcher.famousArticlesDataFetcher())
                                                          .dataFetcher("plan", planDataFetchers.planDataFetcher())
                                                          .dataFetcher("planDetail",
                                                                       planDataFetchers.planDetailDataFetcher())
                                                          .dataFetcher("allPlans",
                                                                       planDataFetchers.allPlanDataFetcher())
                                                          .dataFetcher("allPublishedPlans",
                                                                       planDataFetchers.allPublishedPlansDataFetcher())
                                                          .dataFetcher("allDraftedPlans",
                                                                       planDataFetchers.allDraftedPlansDataFetcher())
                                                          .dataFetcher("allLikedPlans",
                                                                       planDataFetchers.allLikedPlansDataFetcher())
                                                          .dataFetcher("allLearningPlans",
                                                                       planDataFetchers.allLearningPlansDataFetcher())
                                                          .dataFetcher("allLearnedPlans",
                                                                       planDataFetchers.allLearnedPlansDataFetcher())
                                                          .dataFetcher("famousPlans",
                                                                       planDataFetchers.famousPlansDataFetcher())
                                                          .dataFetcher("allPlanElementDetails",
                                                                       planDataFetchers.allPlanElementDetailsDataFetcher())
                                                          .dataFetcher("user", userDataFetcher.userDataFetcher())
                                                          .dataFetcher("famousTags",
                                                                       tagDataFetcher.famousTagDataFetcher()))
                            .type("Mutation",
                                  typeWiring -> typeWiring.dataFetcher("createSection",
                                                                       sectionDataFetcher.createSectionDataFetcher())
                                                          .dataFetcher("updateSection",
                                                                       sectionDataFetcher.updateSectionElementDataFetcher())
                                                          .dataFetcher("likeSection",
                                                                       sectionDataFetcher.likeSectionElementDataFetcher())
                                                          .dataFetcher("deleteLikeSection",
                                                                       sectionDataFetcher.deleteLikeSectionElementDataFetcher())
                                                          .dataFetcher("deleteSection",
                                                                       sectionDataFetcher.deleteSectionElementDataFetcher())
                                                          .dataFetcher("initArticle",
                                                                       articleDataFetcher.initArticleDataFetcher())
                                                          .dataFetcher("updateArticleTitle",
                                                                       articleDataFetcher.updateArticleTitleDataFetcher())
                                                          .dataFetcher("updateArticleImageUrl",
                                                                       articleDataFetcher.updateArticleImageUrlDataFetcher())
                                                          .dataFetcher("updateArticleTags",
                                                                       articleDataFetcher.updateArticleTagsDataFetcher())
                                                          .dataFetcher("deleteArticle",
                                                                       articleDataFetcher.deleteArticleDataFetcher())
                                                          .dataFetcher("publishArticle",
                                                                       articleDataFetcher.publishArticleDataFetcher())
                                                          .dataFetcher("draftArticle",
                                                                       articleDataFetcher.draftArticleDataFetcher())
                                                          .dataFetcher("likeArticle",
                                                                       articleDataFetcher.likeArticleDataFetcher())
                                                          .dataFetcher("deleteLikeArticle",
                                                                       articleDataFetcher.deleteLikeArticleDataFetcher())
                                                          .dataFetcher("finishArticle",
                                                                       articleDataFetcher.finishArticleDataFetcher())
                                                          .dataFetcher("deleteFinishArticle",
                                                                       articleDataFetcher.deleteFinishArticleDataFetcher())
                                                          .dataFetcher("initPlan",
                                                                       planDataFetchers.initPlanDataFetcher())
                                                          .dataFetcher("updatePlanTitle",
                                                                       planDataFetchers.updatePlanTitleDataFetcher())
                                                          .dataFetcher("updatePlanTags",
                                                                       planDataFetchers.updatePlanTagsDataFetcher())
                                                          .dataFetcher("updatePlanDescription",
                                                                       planDataFetchers.updatePlanDescriptionDataFetcher())
                                                          .dataFetcher("updatePlanImageUrl",
                                                                       planDataFetchers.updatePlanImageUrlDataFetcher())
                                                          .dataFetcher("deletePlan",
                                                                       planDataFetchers.deletePlanDataFetcher())
                                                          .dataFetcher("publishPlan",
                                                                       planDataFetchers.publishPlanDataFetcher())
                                                          .dataFetcher("draftPlan",
                                                                       planDataFetchers.draftPlanDataFetcher())
                                                          .dataFetcher("likePlan",
                                                                       planDataFetchers.likePlanDataFetcher())
                                                          .dataFetcher("deleteLikePlan",
                                                                       planDataFetchers.deleteLikePlanDataFetcher())
                                                          .dataFetcher("startPlan",
                                                                       planDataFetchers.startPlanDataFetcher())
                                                          .dataFetcher("stopPlan",
                                                                       planDataFetchers.stopPlanDataFetcher())
                                                          .dataFetcher("finishPlan",
                                                                       planDataFetchers.finishPlanDataFetcher())
                                                          .dataFetcher("createUser",
                                                                       userDataFetcher.createUserDataFetcher())
                                                          .dataFetcher("updateUser",
                                                                       userDataFetcher.updateUserDataFetcher())
                                                          .dataFetcher("updateUserImageUrl",
                                                                       userDataFetcher.updateUserImageDataFetcher())
                                                          .dataFetcher("updateUserTags",
                                                                       userDataFetcher.updateUserTagsDataFetcher())
                                                          .dataFetcher("deleteUser",
                                                                       userDataFetcher.deleteUserDataFetcher())
                                                          .dataFetcher("createTag",
                                                                       tagDataFetcher.createTagDataFetcher()))
                            .build();
    }

    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
