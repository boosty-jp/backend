package co.jp.wever.graphql.domain.service.test;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.test.TestInput;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.test.Test;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.factory.TestFactory;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.test.TestMutationRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.test.TestQueryRepositoryImpl;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestMutationService {
    private final TestQueryRepositoryImpl testQueryRepository;
    private final TestMutationRepositoryImpl testMutationRepository;

    public TestMutationService(
        TestQueryRepositoryImpl testQueryRepository, TestMutationRepositoryImpl testMutationRepository) {
        this.testQueryRepository = testQueryRepository;
        this.testMutationRepository = testMutationRepository;
    }

    public String publish(TestInput testInput, Requester requester) {
        log.info("publish test: {}", testInput);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Test test = TestFactory.make(testInput);

        if (isForbiddenAuthor(testInput.getId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (!test.canPublish()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.CANNOT_PUBLISH_ARTICLE.getString());
        }

        if (isEntry(testInput.getId())) {
            return testMutationRepository.publishByEntry(test, requester.getUserId());
        } else {
//            articleMutationRepository.publish(articleInput.getId(), article, requester.getUserId());
//            return articleInput.getId();
            return "";
        }
    }

    private boolean isEntry(String testId) {
        return StringUtil.isNullOrEmpty(testId);
    }

    private boolean isForbiddenAuthor(String testId, String userId) {
        // IDがない場合は新規投稿
        if (isEntry(testId)) {
            return false;
        }

        return !isOwner(testId, userId);
    }

    private boolean isOwner(String testId, String userId) {
        // 新規投稿じゃなければ、更新できるユーザーかチェックする
        UserId authorId = UserId.of(testQueryRepository.findAuthorId(testId));
        UserId requesterId = UserId.of(userId);
        return authorId.same(requesterId);
    }
}
