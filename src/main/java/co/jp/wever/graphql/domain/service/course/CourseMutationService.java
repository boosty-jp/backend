package co.jp.wever.graphql.domain.service.course;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.course.CourseInput;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.course.Course;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.factory.CourseFactory;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.ArticleQueryRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.course.CourseMutationRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.course.CourseQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseMutationService {
    private final CourseMutationRepositoryImpl courseMutationRepository;
    private final CourseQueryRepositoryImpl courseQueryRepository;
    private final ArticleQueryRepositoryImpl articleQueryRepository;

    public CourseMutationService(
        CourseMutationRepositoryImpl courseMutationRepository,
        CourseQueryRepositoryImpl courseQueryRepository,
        ArticleQueryRepositoryImpl articleQueryRepository) {
        this.courseMutationRepository = courseMutationRepository;
        this.courseQueryRepository = courseQueryRepository;
        this.articleQueryRepository = articleQueryRepository;
    }


    public String publish(CourseInput courseInput, Requester requester) {
        log.info("publish course: {}", courseInput);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Course course = CourseFactory.make(courseInput);

        if (isForbiddenAuthor(course, courseInput.getId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (!course.valid()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DUPLICATE_CONTENTS_DATA.getString());
        }

        if (course.getArticleIds().size() != articleQueryRepository.publishedArticleCount(course.getArticleIds())) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NOT_PUBLISHED_ARTICLE.getString());
        }

        if (course.getBase().isEntry()) {
            return courseMutationRepository.publishByEntry(course, requester.getUserId());
        } else {
            courseMutationRepository.publish(courseInput.getId(), course, requester.getUserId());
            return courseInput.getId();
        }
    }

    public String draft(CourseInput courseInput, Requester requester) {
        log.info("publish course: {}", courseInput);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Course course = CourseFactory.make(courseInput);

        if (isForbiddenAuthor(course, courseInput.getId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (!course.valid()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_COURSE_DATA.getString());
        }

        if (course.getArticleIds().size() != articleQueryRepository.publishedArticleCount(course.getArticleIds())) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NOT_PUBLISHED_ARTICLE.getString());
        }

        if (course.getBase().isEntry()) {
            return courseMutationRepository.draftByEntry(course, requester.getUserId());
        } else {
            courseMutationRepository.draft(courseInput.getId(), course, requester.getUserId());
            return courseInput.getId();
        }
    }

    public void delete(String courseId, Requester requester) {
        log.info("delete articleId: {}", courseId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(courseId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        courseMutationRepository.delete(courseId, requester.getUserId());
    }

    public void like(String courseId, Requester requester) {
        log.info("like articleId: {}", courseId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        courseMutationRepository.like(courseId, requester.getUserId());
    }

    public void clearLike(String courseId, Requester requester) {
        log.info("delete articleId: {}", courseId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        courseMutationRepository.clearLike(courseId, requester.getUserId());
    }

    public boolean start(String courseId, Requester requester) {
        log.info("like articleId: {}", courseId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        // 進捗が100%だったら完了にする
        if (courseQueryRepository.findLearnList(courseId, requester.getUserId()).stream().allMatch(i -> i > 0)) {
            courseMutationRepository.complete(courseId, requester.getUserId());
             return true;
        } else{
            courseMutationRepository.start(courseId, requester.getUserId());
            return false;
        }
    }

    public void clearStart(String courseId, Requester requester) {
        log.info("delete articleId: {}", courseId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        courseMutationRepository.clearStart(courseId, requester.getUserId());
    }

    private boolean isForbiddenAuthor(Course course, String courseId, String userId) {
        if (course.getBase().isEntry()) {
            return false;
        }

        return !isOwner(courseId, userId);
    }

    private boolean isOwner(String courseId, String userId) {
        // 新規投稿じゃなければ、更新できるユーザーかチェックする
        UserId authorId = UserId.of(courseQueryRepository.findAuthorId(courseId));
        UserId requesterId = UserId.of(userId);
        return authorId.same(requesterId);
    }
}
