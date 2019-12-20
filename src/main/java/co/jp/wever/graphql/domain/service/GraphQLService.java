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
import co.jp.wever.graphql.domain.service.datafetchers.CourseDataFetcher;
import co.jp.wever.graphql.domain.service.datafetchers.SkillDataFetcher;
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
    private CourseDataFetcher courseDataFetcher;

    @Autowired
    private ArticleDataFetcher articleDataFetcher;

    @Autowired
    private UserDataFetcher userDataFetcher;

    @Autowired
    private TagDataFetcher tagDataFetcher;

    @Autowired
    private SkillDataFetcher skillDataFetcher;

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
                                                          .dataFetcher("createdArticles",
                                                                       articleDataFetcher.createdArticlesDataFetcher())
                                                          .dataFetcher("createdArticlesBySelf",
                                                                       articleDataFetcher.createdArticlesBySelfDataFetcher())
                                                          .dataFetcher("actionedArticles",
                                                                       articleDataFetcher.actionedArticlesDataFetcher())
                                                          .dataFetcher("actionedArticlesBySelf",
                                                                       articleDataFetcher.actionedArticlesBySelfDataFetcher())
                                                          .dataFetcher("famousArticles",
                                                                       articleDataFetcher.famousArticlesDataFetcher())
                                                          .dataFetcher("course", courseDataFetcher.courseDataFetcher())
                                                          .dataFetcher("allCourses",
                                                                       courseDataFetcher.allCourseDataFetcher())
                                                          .dataFetcher("allPublishedCourses",
                                                                       courseDataFetcher.allPublishedCoursesDataFetcher())
                                                          .dataFetcher("allDraftedCourses",
                                                                       courseDataFetcher.allDraftedCoursesDataFetcher())
                                                          .dataFetcher("allLikedCourses",
                                                                       courseDataFetcher.allLikedCoursesDataFetcher())
                                                          .dataFetcher("allLearningCourses",
                                                                       courseDataFetcher.allLearningCoursesDataFetcher())
                                                          .dataFetcher("allLearnedCourses",
                                                                       courseDataFetcher.allLearnedCoursesDataFetcher())
                                                          .dataFetcher("famousCourses",
                                                                       courseDataFetcher.famousCoursesDataFetcher())
                                                          .dataFetcher("user", userDataFetcher.userDataFetcher())
                                                          .dataFetcher("account", userDataFetcher.accountDataFetcher())
                                                          .dataFetcher("famousTags",
                                                                       tagDataFetcher.famousTagDataFetcher())
                                                          .dataFetcher("famousSkills",
                                                                       skillDataFetcher.famousSkillDataFetcher()))
                            .type("Mutation",
                                  typeWiring -> typeWiring.dataFetcher("publishArticle",
                                                                       articleDataFetcher.publishArticleDataFetcher())
                                                          .dataFetcher("draftArticle",
                                                                       articleDataFetcher.draftArticleDataFetcher())
                                                          .dataFetcher("deleteArticle",
                                                                       articleDataFetcher.deleteArticleDataFetcher())
                                                          .dataFetcher("likeArticle",
                                                                       articleDataFetcher.likeArticleDataFetcher())
                                                          .dataFetcher("clearLikeArticle",
                                                                       articleDataFetcher.clearLikeArticleDataFetcher())
                                                          .dataFetcher("learnArticle",
                                                                       articleDataFetcher.learnArticleDataFetcher())
                                                          .dataFetcher("clearLearnArticle",
                                                                       articleDataFetcher.clearLearnArticleDataFetcher())
                                                          .dataFetcher("publishCourse",
                                                                       courseDataFetcher.publishCourseDataFetcher())
                                                          .dataFetcher("draftCourse",
                                                                       courseDataFetcher.draftCourseDataFetcher())
                                                          .dataFetcher("deleteCourse",
                                                                       courseDataFetcher.deleteCourseDataFetcher())
                                                          .dataFetcher("likeCourse",
                                                                       courseDataFetcher.likeCourseDataFetcher())
                                                          .dataFetcher("clearLikeCourse",
                                                                       courseDataFetcher.clearLikeCourseDataFetcher())
                                                          .dataFetcher("startCourse",
                                                                       courseDataFetcher.startCourseDataFetcher())
                                                          .dataFetcher("clearStartCourse",
                                                                       courseDataFetcher.clearStartCourseDataFetcher())
                                                          .dataFetcher("createUser",
                                                                       userDataFetcher.createUserDataFetcher())
                                                          .dataFetcher("updateUser",
                                                                       userDataFetcher.updateUserDataFetcher())
                                                          .dataFetcher("updateUserSetting",
                                                                       userDataFetcher.updateUserSettingDataFetcher())
                                                          .dataFetcher("deleteUser",
                                                                       userDataFetcher.deleteUserDataFetcher())
                                                          .dataFetcher("createTag",
                                                                       tagDataFetcher.createTagDataFetcher())
                                                          .dataFetcher("createSkill",
                                                                       skillDataFetcher.createSkillDataFetcher()))
                            .build();
    }

    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
