package co.jp.wever.graphql.infrastructure.repository.test;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.domain.repository.test.TestQueryRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.test.TestEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.TestEntity;

@Component
public class TestQueryRepositoryImpl implements TestQueryRepository {

    private final NeptuneClient neptuneClient;

    public TestQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public TestEntity findOne(String testId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(testId)
                                         .hasLabel(VertexLabel.TEST.getString())
                                         .project("base", "questions", "author", "status")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.QUESTIONS.getString())
                                               .project("textQuestions", "selectQuestions")
                                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                                     .hasLabel(VertexLabel.TEXT_QUESTION.getString())
                                                     .project("number", "textQuestion")
                                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                                     .by(__.valueMap().with(WithOptions.tokens))
                                                     .fold())
                                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                                     .hasLabel(VertexLabel.SELECT_QUESTION.getString())
                                                     .project("number", "selectQuestion", "candidates")
                                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                                     .by(__.valueMap().with(WithOptions.tokens))
                                                     .by(__.out(EdgeLabel.INCLUDE.getString())
                                                           .hasLabel(VertexLabel.SELECT_ANSWER.getString())
                                                           .project("number", "answer")
                                                           .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                                 .values(IncludeEdgeProperty.NUMBER.getString()))
                                                           .by(__.valueMap().with(WithOptions.tokens))
                                                           .fold())
                                                     .fold()))
                                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                                   EdgeLabel.DELETE.getString(),
                                                   EdgeLabel.PUBLISH.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                                    EdgeLabel.DELETE.getString(),
                                                    EdgeLabel.PUBLISH.getString()).label())
                                         .next();

        return TestEntityConverter.toTestEntity(allResult);
    }

    @Override
    public TestEntity findOneForGuest(String testId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return null;
    }

    @Override
    public String findAuthorId(String testId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return g.V(testId)
                .hasLabel(VertexLabel.TEST.getString())
                .in(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString())
                .hasLabel(VertexLabel.USER.getString())
                .id()
                .next()
                .toString();
    }
}
