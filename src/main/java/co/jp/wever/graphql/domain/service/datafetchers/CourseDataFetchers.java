package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.course.FamousCourseResponseConverter;
import co.jp.wever.graphql.application.converter.course.LearningCourseItemResponseConverter;
import co.jp.wever.graphql.application.converter.course.CourseBaseInputConverter;
import co.jp.wever.graphql.application.converter.course.CourseElementDetailResponseConverter;
import co.jp.wever.graphql.application.converter.course.CourseElementInputsConverter;
import co.jp.wever.graphql.application.converter.course.CourseDetailResponseConverter;
import co.jp.wever.graphql.application.converter.course.CourseListItemResponseConverter;
import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.datamodel.request.CourseBaseInput;
import co.jp.wever.graphql.application.datamodel.request.CourseElementInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.domainmodel.course.element.FindCourseElementDetail;
import co.jp.wever.graphql.domain.service.course.CreateCourseService;
import co.jp.wever.graphql.domain.service.course.DeleteCourseService;
import co.jp.wever.graphql.domain.service.course.FindCourseService;
import co.jp.wever.graphql.domain.service.course.UpdateCourseService;
import graphql.schema.DataFetcher;

@Component
public class CourseDataFetchers {

    private final FindCourseService findCourseService;
    private final UpdateCourseService updateCourseService;
    private final DeleteCourseService deleteCourseService;
    private final CreateCourseService createCourseService;
    private final RequesterConverter requesterConverter;

    CourseDataFetchers(
        FindCourseService findCourseService,
        UpdateCourseService updateCourseService,
        DeleteCourseService deleteCourseService,
        CreateCourseService createCourseService,
        RequesterConverter requesterConverter) {
        this.findCourseService = findCourseService;
        this.updateCourseService = updateCourseService;
        this.deleteCourseService = deleteCourseService;
        this.createCourseService = createCourseService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher courseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            return CourseListItemResponseConverter.toCourseListItemResponse(this.findCourseService.findOne(courseId,
                                                                                                     requester));
        };
    }

    public DataFetcher courseDetailDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            return CourseDetailResponseConverter.toCourseResponse(this.findCourseService.findDetail(courseId, requester));
        };
    }

    public DataFetcher allCourseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            return this.findCourseService.findAllCourse(requester)
                                       .stream()
                                       .map(p -> CourseListItemResponseConverter.toCourseListItemResponse(p))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");

            return this.findCourseService.findAllPublishedCourse(userId)
                                       .stream()
                                       .map(p -> CourseListItemResponseConverter.toCourseListItemResponse(p))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allDraftedCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allLikedCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            return this.findCourseService.findAllLikedCourse(requester)
                                       .stream()
                                       .map(p -> FamousCourseResponseConverter.toFamousCourseResponse(p))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allLearningCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            return this.findCourseService.findAllLearningCourse(requester)
                                       .stream()
                                       .map(r -> LearningCourseItemResponseConverter.toLearningCourseItemResponse(r))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allLearnedCoursesDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher famousCoursesDataFetcher() {
        return dataFetchingEnvironment -> findCourseService.findFamous()
                                                         .stream()
                                                         .map(p -> FamousCourseResponseConverter.toFamousCourseResponse(p))
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
                                         .map(p -> CourseElementDetailResponseConverter.toCourseElementDetailResponse(p))
                                         .collect(Collectors.toList());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = createCourseService.initCourse(requester);

            return CreateResponse.builder().id(courseId).build();
        };
    }

    public DataFetcher updateCourseTitleDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String title = dataFetchingEnvironment.getArgument("title");
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.updateCourseTitle(courseId, title, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateCourseTagsDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");
            List<String> tags = dataFetchingEnvironment.getArgument("tags");

            updateCourseService.updateCourseTags(courseId, tags, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateCourseImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String courseId = dataFetchingEnvironment.getArgument("courseId");
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updateCourseService.updateCourseImageUrl(courseId, imageUrl, requester);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updateCourseDescriptionDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String courseId = dataFetchingEnvironment.getArgument("courseId");
            String description = dataFetchingEnvironment.getArgument("description");

            updateCourseService.updateCourseDescription(courseId, description, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteCourseDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String courseId = dataFetchingEnvironment.getArgument("courseId");

            deleteCourseService.deleteCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            CourseBaseInput baseInput = CourseBaseInputConverter.toCourseBaseInput(dataFetchingEnvironment);
            List<CourseElementInput> elementsInput =
                CourseElementInputsConverter.toCourseElementsInput(dataFetchingEnvironment);

            updateCourseService.publishCourse(baseInput, elementsInput, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            CourseBaseInput baseInput = CourseBaseInputConverter.toCourseBaseInput(dataFetchingEnvironment);
            List<CourseElementInput> elementsInput =
                CourseElementInputsConverter.toCourseElementsInput(dataFetchingEnvironment);

            updateCourseService.draftCourse(baseInput, elementsInput, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher likeCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.likeCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteLikeCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.deleteLikeCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher startCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.startCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher finishCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.finishCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher stopCourseDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String courseId = dataFetchingEnvironment.getArgument("courseId");

            updateCourseService.stopCourse(courseId, requester);
            return UpdateResponse.builder().build();
        };
    }
}
