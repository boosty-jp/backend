package co.jp.wever.graphql.domain.service.test;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.datamodel.test.TestEntity;
import co.jp.wever.graphql.infrastructure.repository.test.TestQueryRepositoryImpl;

@Service
public class TestQueryService {
    private final TestQueryRepositoryImpl testQueryRepository;

    public TestQueryService(TestQueryRepositoryImpl testQueryRepository) {
        this.testQueryRepository = testQueryRepository;
    }

    public TestEntity findTest(String testId, Requester requester) {
        TestEntity testEntity;
        if (requester.isGuest()) {
            testEntity = testQueryRepository.findOneForGuest(testId);
        } else {
            testEntity = testQueryRepository.findOne(testId, requester.getUserId());
        }

        if (!canRead(testEntity, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return testEntity;
    }

    private boolean canRead(TestEntity testEntity, String requesterId) {
        if (testEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        return requesterId.equals(testEntity.getAuthor().getUserId());
    }
}
