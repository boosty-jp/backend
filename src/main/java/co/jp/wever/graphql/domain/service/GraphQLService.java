package co.jp.wever.graphql.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import co.jp.wever.graphql.domain.service.datafetchers.ArticleDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.PlanDataFetchers;
import co.jp.wever.graphql.domain.service.datafetchers.SectionDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.TagDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.UserDataFetcher;
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
        File schemaFile = new ClassPathResource("schema.graphql").getFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
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
                                                          .dataFetcher("relatedSections",
                                                                       sectionDataFetcher.relatedSectionsDataFetcher())
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
                                                          .dataFetcher("relatedArticles",
                                                                       articleDataFetcher.relatedArticlesDataFetcher())
                                                          .dataFetcher("plan", planDataFetchers.planDataFetcher())
                                                          //                                                          .dataFetcher("allPlans",
                                                          //                                                                       planDataFetchers.allPlanDataFetcher())
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
                                                          .dataFetcher("relatedPlans",
                                                                       planDataFetchers.relatedPlansDataFetcher())
                                                          .dataFetcher("user", userDataFetcher.userDataFetcher()))
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
                                                          //                                                          .dataFetcher("updateArticle",
                                                          //                                                                       articleDataFetcher.updateArticleDataFetcher())
                                                          .dataFetcher("updateArticleTitle",
                                                                       articleDataFetcher.updateArticleTitleDataFetcher())
                                                          .dataFetcher("updateArticleImageUrl",
                                                                       articleDataFetcher.updateArticleImageUrlDataFetcher())
                                                          .dataFetcher("updateArticleTags",
                                                                       articleDataFetcher.updateArticleImageUrlDataFetcher())
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
                                                          .dataFetcher("createPlanBase",
                                                                       planDataFetchers.createPlanBaseDataFetcher())
                                                          .dataFetcher("updatePlanBase",
                                                                       planDataFetchers.updatePlanBaseDataFetcher())
                                                          .dataFetcher("createPlanElements",
                                                                       planDataFetchers.createPlanElementsDataFetcher())
                                                          .dataFetcher("updatePlanElements",
                                                                       planDataFetchers.updatePlanElementsDataFetcher())
                                                          .dataFetcher("deletePlan",
                                                                       planDataFetchers.deletePlanDataFetcher())
                                                          .dataFetcher("publishPlan",
                                                                       planDataFetchers.publishPlanDataFetcher())
                                                          .dataFetcher("draftPlan",
                                                                       planDataFetchers.draftPlanDataFetcher())
                                                          .dataFetcher("startPlan",
                                                                       planDataFetchers.startPlanDataFetcher())
                                                          .dataFetcher("stopPlan",
                                                                       planDataFetchers.stopPlanDataFetcher())
                                                          .dataFetcher("createUser",
                                                                       userDataFetcher.createUserDataFetcher())
                                                          .dataFetcher("updateUser",
                                                                       userDataFetcher.updateUserDataFetcher())
                                                          .dataFetcher("deleteUser",
                                                                       userDataFetcher.deleteUserDataFetcher())
                                                          .dataFetcher("followUser",
                                                                       userDataFetcher.followUserDataFetcher())
                                                          .dataFetcher("unFollowUser",
                                                                       userDataFetcher.unFollowUserDataFetcher())
                                                          .dataFetcher("createTag",
                                                                       tagDataFetcher.createTagDataFetcher()))
                            .build();
    }

    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
