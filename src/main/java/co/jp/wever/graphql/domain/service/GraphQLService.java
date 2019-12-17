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
import co.jp.wever.graphql.domain.service.datafetchers.CourseDataFetchers;
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
    private CourseDataFetchers courseDataFetchers;

    @Autowired
    private ArticleDataFetcher articleDataFetcher;

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
                                  typeWiring -> typeWiring.dataFetcher("article",
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
                                                          .dataFetcher("course", courseDataFetchers.courseDataFetcher())
                                                          .dataFetcher("allCourses",
                                                                       courseDataFetchers.allCourseDataFetcher())
                                                          .dataFetcher("allPublishedCourses",
                                                                       courseDataFetchers.allPublishedCoursesDataFetcher())
                                                          .dataFetcher("allDraftedCourses",
                                                                       courseDataFetchers.allDraftedCoursesDataFetcher())
                                                          .dataFetcher("allLikedCourses",
                                                                       courseDataFetchers.allLikedCoursesDataFetcher())
                                                          .dataFetcher("allLearningCourses",
                                                                       courseDataFetchers.allLearningCoursesDataFetcher())
                                                          .dataFetcher("allLearnedCourses",
                                                                       courseDataFetchers.allLearnedCoursesDataFetcher())
                                                          .dataFetcher("famousCourses",
                                                                       courseDataFetchers.famousCoursesDataFetcher())
                                                          .dataFetcher("user", userDataFetcher.userDataFetcher())
                                                          .dataFetcher("account", userDataFetcher.accountDataFetcher())
                                                          .dataFetcher("famousSkills",
                                                                       userDataFetcher.profileDataFetcher())
                                                          .dataFetcher("famousTags",
                                                                       tagDataFetcher.famousTagDataFetcher()))
                            .type("Mutation",
                                  typeWiring -> typeWiring.dataFetcher("publishArticle",
                                                                       articleDataFetcher.publishArticleDataFetcher())
                                                          .dataFetcher("draftArticle",
                                                                       articleDataFetcher.draftArticleDataFetcher())
                                                          .dataFetcher("deleteArticle",
                                                                       articleDataFetcher.deleteArticleDataFetcher())
                                                          .dataFetcher("likeArticle",
                                                                       articleDataFetcher.likeArticleDataFetcher())
                                                          .dataFetcher("deleteLikeArticle",
                                                                       articleDataFetcher.deleteLikeArticleDataFetcher())
                                                          .dataFetcher("learnArticle",
                                                                       articleDataFetcher.finishArticleDataFetcher())
                                                          .dataFetcher("deleteLearnArticle",
                                                                       articleDataFetcher.deleteFinishArticleDataFetcher())
                                                          .dataFetcher("publishCourse",
                                                                       courseDataFetchers.publishCourseDataFetcher())
                                                          .dataFetcher("draftCourse",
                                                                       courseDataFetchers.draftCourseDataFetcher())
                                                          .dataFetcher("deleteCourse",
                                                                       courseDataFetchers.deleteCourseDataFetcher())
                                                          .dataFetcher("likeCourse",
                                                                       courseDataFetchers.likeCourseDataFetcher())
                                                          .dataFetcher("deleteLikeCourse",
                                                                       courseDataFetchers.deleteLikeCourseDataFetcher())
                                                          .dataFetcher("startCourse",
                                                                       courseDataFetchers.startCourseDataFetcher())
                                                          .dataFetcher("deleteStartCourse",
                                                                       courseDataFetchers.stopCourseDataFetcher())
                                                          .dataFetcher("createUser",
                                                                       userDataFetcher.createUserDataFetcher())
                                                          .dataFetcher("updateUser",
                                                                       userDataFetcher.updateUserDataFetcher())
                                                          .dataFetcher("updateUserSetting",
                                                                       userDataFetcher.updateUserSettingDataFetcher())
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
