package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.SearchConditionConverter;
import co.jp.wever.graphql.application.converter.article.ArticleResponseConverter;
import co.jp.wever.graphql.application.converter.course.CourseInputConverter;
import co.jp.wever.graphql.application.converter.course.CourseResponseConverter;
import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.datamodel.request.CourseInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.SearchConditionInput;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.course.CourseMutationService;
import co.jp.wever.graphql.domain.service.course.CourseQueryService;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
import graphql.schema.DataFetcher;

@Component
public class CourseDataFetcher {

    private final CourseMutationService courseMutationService;
    private final CourseQueryService courseQueryService;
    private final RequesterConverter requesterConverter;

    public CourseDataFetcher(
        CourseMutationService courseMutationService,
        CourseQueryService courseQueryService,
        RequesterConverter requesterConverter) {
        this.courseMutationService = courseMutationService;
        this.courseQueryService = courseQueryService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher courseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            return CourseResponseConverter.toCourseResponse(courseQueryService.findCourse(courseId, requester));
        };
    }


    public DataFetcher createdCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            List<CourseEntity> results = courseQueryService.(userId, searchConditionInput);
            return results.stream()
                          .map(r -> ArticleResponseConverter.toArticleResponseForPublishedList(r))
                          .collect(Collectors.toList());
        };
    }

    public DataFetcher createdArticlesBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            List<ArticleEntity> results =
                articleQueryService.findCreatedArticlesBySelf(requester, searchConditionInput);
            return results.stream()
                          .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                          .collect(Collectors.toList());
        };
    }

    public DataFetcher actionedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            List<ArticleEntity> results = articleQueryService.findActionedArticles(userId, searchConditionInput);
            return results.stream()
                          .map(r -> ArticleResponseConverter.toArticleResponseForPublishedList(r))
                          .collect(Collectors.toList());
        };
    }

    public DataFetcher actionedArticlesBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            List<ArticleEntity> results =
                articleQueryService.findActionedArticlesBySelf(requester, searchConditionInput);
            return results.stream()
                          .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                          .collect(Collectors.toList());
        };
    }

    public DataFetcher famousCoursesDataFetcher() {
        return dataFetchingEnvironment -> findCourseService.findFamous()
                                                           .stream()
                                                           .map(p -> FamousCourseResponseConverter.toFamousCourseResponse(
                                                               p))
                                                           .collect(Collectors.toList());
    }

    public DataFetcher relatedCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allCourseElementDetailsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");
            List<FindCourseElementDetail> findCourseElementDetails =
                findCourseService.findAllCourseElementDetails(courseId, requester);
            return findCourseElementDetails.stream()
                                           .map(p -> CourseElementDetailResponseConverter.toCourseElementDetailResponse(
                                               p))
                                           .collect(Collectors.toList());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher deleteCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            courseMutationService.delete(courseId, requester);
            return true;
        };
    }

    public DataFetcher publishCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            CourseInput courseInput = CourseInputConverter.toCourseInput(dataFetchingEnvironment);

            String id = courseMutationService.publish(courseInput, requester);
            return CreateResponse.builder().id(id).build();
        };
    }

    public DataFetcher draftCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            CourseInput courseInput = CourseInputConverter.toCourseInput(dataFetchingEnvironment);

            String id = courseMutationService.draft(courseInput, requester);
            return CreateResponse.builder().id(id).build();
        };
    }

    public DataFetcher likeCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            courseMutationService.like(courseId, requester);
            return true;
        };
    }

    public DataFetcher clearLikeCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            courseMutationService.clearLike(courseId, requester);
            return true;
        };
    }

    public DataFetcher startCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            return courseMutationService.start(courseId, requester);
        };
    }

    public DataFetcher clearStartCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            courseMutationService.clearStart(courseId, requester);
            return true;
        };
    }
}
