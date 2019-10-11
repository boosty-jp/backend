package co.jp.wever.graphql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import co.jp.wever.graphql.service.datafetchers.ArticleDataFetcher;
import co.jp.wever.graphql.service.datafetchers.PlanDataFetchers;
import co.jp.wever.graphql.service.datafetchers.SectionDataFetcher;
import co.jp.wever.graphql.service.datafetchers.TagDataFetcher;
import co.jp.wever.graphql.service.datafetchers.UserDataFetcher;
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
        return RuntimeWiring.newRuntimeWiring().type("Query",
            typeWiring -> typeWiring.dataFetcher("section", sectionDataFetcher.sectionDataFetcher()).dataFetcher("allLikedSections", sectionDataFetcher.allLikedSectionsDataFetcher())
                .dataFetcher("allBookmarkedSections", sectionDataFetcher.allBookmarkedSectionsDataFetcher()).dataFetcher("famousSections", sectionDataFetcher.famousSectionsDataFetcher())
                .dataFetcher("relatedSections", sectionDataFetcher.relatedSectionsDataFetcher()).dataFetcher("article", articleDataFetcher.articleDataFetcher())
                .dataFetcher("allArticles", articleDataFetcher.allArticlesDataFetcher()).dataFetcher("allPublishedArticles", articleDataFetcher.allPublishedArticlesDataFetcher())
                .dataFetcher("allDraftedArticles", articleDataFetcher.allDraftedArticlesDataFetcher()).dataFetcher("allLikedArticles", articleDataFetcher.allLikedArticlesDataFetcher())
                .dataFetcher("allLearnedArticles", articleDataFetcher.allLearnedArticlesDataFetcher()).dataFetcher("allBookmarkedArticles", articleDataFetcher.allBookmarkedArticlesDataFetcher())
                .dataFetcher("famousArticles", articleDataFetcher.famousArticlesDataFetcher()).dataFetcher("relatedArticles", articleDataFetcher.relatedArticlesDataFetcher())
                .dataFetcher("plan", planDataFetchers.planDataFetcher()).dataFetcher("allPlans", planDataFetchers.allPlanDataFetcher())
                .dataFetcher("allPublishedPlans", planDataFetchers.allPublishedPlansDataFetcher()).dataFetcher("allDraftedPlans", planDataFetchers.allDraftedPlansDataFetcher())
                .dataFetcher("allLikedPlans", planDataFetchers.allLikedPlansDataFetcher()).dataFetcher("allLearningPlans", planDataFetchers.allLearningPlansDataFetcher())
                .dataFetcher("allLearnedPlans", planDataFetchers.allLearnedPlansDataFetcher()).dataFetcher("famousPlans", planDataFetchers.famousPlansDataFetcher())
                .dataFetcher("relatedPlans", planDataFetchers.relatedPlansDataFetcher())).type("Mutation",
            typeWiring -> typeWiring.dataFetcher("addSection", sectionDataFetcher.addSectionDataFetcher()).dataFetcher("updateSection", sectionDataFetcher.updateSectionElementDataFetcher())
                .dataFetcher("bookmarkSection", sectionDataFetcher.bookmarkSectionElementDataFetcher()).dataFetcher("likeSection", sectionDataFetcher.likeSectionElementDataFetcher())
                .dataFetcher("initArticle", articleDataFetcher.initArticleDataFetcher()).dataFetcher("updateArticle", articleDataFetcher.updateArticlesElementDataFetcher())
                .dataFetcher("deleteArticle", articleDataFetcher.deleteArticlesElementDataFetcher()).dataFetcher("publishArticle", articleDataFetcher.publishArticlesElementDataFetcher())
                .dataFetcher("draftArticle", articleDataFetcher.draftArticlesElementDataFetcher()).dataFetcher("bookmarkArticle", articleDataFetcher.bookmarkArticlesElementDataFetcher())
                .dataFetcher("likeArticle", articleDataFetcher.likeArticlesElementDataFetcher()).dataFetcher("initPlan", planDataFetchers.initPlanDataFetcher())
                .dataFetcher("addPlanElement", planDataFetchers.addPlansElementDataFetcher()).dataFetcher("updatePlan", planDataFetchers.updatePlanDataFetcher())
                .dataFetcher("deletePlan", planDataFetchers.deletePlanDataFetcher()).dataFetcher("publishPlan", planDataFetchers.publishPlanDataFetcher())
                .dataFetcher("draftPlan", planDataFetchers.draftPlanDataFetcher()).dataFetcher("startPlan", planDataFetchers.startPlanDataFetcher())
                .dataFetcher("followUser", userDataFetcher.followUserDataFetcher()).dataFetcher("unFollowUser", userDataFetcher.unFollowUserDataFetcher())
                .dataFetcher("followTag", tagDataFetcher.followTagDataFetcher())).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
