package co.jp.wever.graphql.infrastructure.repository.test;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import co.jp.wever.graphql.domain.domainmodel.test.Question;
import co.jp.wever.graphql.domain.domainmodel.test.Test;
import co.jp.wever.graphql.domain.domainmodel.test.explanation.Explanation;
import co.jp.wever.graphql.domain.repository.test.TestMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ExplanationVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SelectQuestionAnswerVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SelectQuestionVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TestVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TextQuestionVertexProperty;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

@Component
public class TestMutationRepositoryImpl implements TestMutationRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public TestMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    public void publish(String testId, Test test, String userId) {

    }

    public String publishByEntry(Test test, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String testId = createTestVertex(g, test, now);

        addQuestions(g, testId, test, now);
        addStatus(g, testId, EdgeLabel.PUBLISH.getString(), userId, now);

        //        algoliaClient.getArticleIndex()
        //                     .saveObjectAsync(ArticleSearchEntityConverter.toArticleSearchEntity(articleId, article, now));


        return testId;
    }

    public void draft(String testId, Test test, String userId) {

    }

    public String draftByEntry(Test test, String userId) {
        return "";
    }

    private String createTestVertex(GraphTraversalSource g, Test test, long now) {
        return g.addV(VertexLabel.TEST.getString())
                .property(TestVertexProperty.TITLE.getString(), test.getTitle())
                .property(TestVertexProperty.DESCRIPTION.getString(), test.getDescription())
                .property(DateProperty.CREATE_TIME.getString(), now)
                .property(DateProperty.UPDATE_TIME.getString(), now)
                .id()
                .next()
                .toString();
    }

    private void addQuestions(GraphTraversalSource g, String testId, Test test, long now) {
        String questionsId = g.addV(VertexLabel.QUESTIONS.getString())
                              .property(DateProperty.CREATE_TIME.getString(), now)
                              .id()
                              .next()
                              .toString();

        g.V(questionsId)
         .hasLabel(VertexLabel.QUESTIONS.getString())
         .addE(EdgeLabel.INCLUDE.getString())
         .from(g.V(testId).hasLabel(VertexLabel.TEST.getString()))
         .next();

        AtomicInteger idx = new AtomicInteger();
        test.getQuestions().getQuestions().forEach(question -> {
            if (question.getType().isText()) {
                createTextQuestionVertex(g, questionsId, question, now, idx);
            } else if (question.getType().isSelect()) {
                createSelectQuestionVertex(g, questionsId, question, now, idx);
            }
            idx.getAndIncrement();
        });
    }

    private void createTextQuestionVertex(
        GraphTraversalSource g, String questionsId, Question question, long now, AtomicInteger idx) {
        String textQuestionId = g.addV(VertexLabel.TEXT_QUESTION.getString())
                                 .property(TextQuestionVertexProperty.QUESTION_TEXT.getString(),
                                           question.getQuestionText().getValue())
                                 .property(TextQuestionVertexProperty.ANSWER.getString(),
                                           question.getTextAnswer().getAnswer())
                                 .property(TextQuestionVertexProperty.SHOW_COUNT.getString(),
                                           question.getTextAnswer().isShowCount())
                                 .property(DateProperty.CREATE_TIME.getString(), now)
                                 .property(DateProperty.UPDATE_TIME.getString(), now)
                                 .id()
                                 .next()
                                 .toString();

        g.V(textQuestionId)
         .addE(EdgeLabel.INCLUDE.getString())
         .property(IncludeEdgeProperty.NUMBER.getString(), idx.get())
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(questionsId).hasLabel(VertexLabel.QUESTIONS.getString()))
         .next();

        AtomicInteger explanationIdx = new AtomicInteger();
        question.getExplanations().getExplanations().forEach(e -> {
            createExplanationVertex(g, textQuestionId, e, now, explanationIdx, VertexLabel.TEXT_QUESTION.toString());
            explanationIdx.getAndIncrement();
        });
    }

    private void createSelectQuestionVertex(
        GraphTraversalSource g, String questionsId, Question question, long now, AtomicInteger questionIdx) {
        String selectQuestionId = g.addV(VertexLabel.SELECT_QUESTION.getString())
                                   .property(SelectQuestionVertexProperty.QUESTION_TEXT.getString(),
                                             question.getQuestionText().getValue())
                                   .property(DateProperty.CREATE_TIME.getString(), now)
                                   .property(DateProperty.UPDATE_TIME.getString(), now)
                                   .id()
                                   .next()
                                   .toString();

        g.V(selectQuestionId)
         .addE(EdgeLabel.INCLUDE.getString())
         .property(IncludeEdgeProperty.NUMBER.getString(), questionIdx.get())
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(questionsId).hasLabel(VertexLabel.QUESTIONS.getString()))
         .next();

        AtomicInteger answerIdx = new AtomicInteger();
        question.getSelectAnswers().getSelectAnswers().forEach(s -> {
            g.addV(VertexLabel.SELECT_ANSWER.getString())
             .property(SelectQuestionAnswerVertexProperty.IS_ANSWER.getString(), s.isAnswer())
             .property(SelectQuestionAnswerVertexProperty.TEXT.getString(), s.getText().getValue())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .property(DateProperty.UPDATE_TIME.getString(), now)
             .addE(EdgeLabel.INCLUDE.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), answerIdx.get())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(selectQuestionId).hasLabel(VertexLabel.SELECT_QUESTION.getString()))
             .next();

            answerIdx.getAndIncrement();
        });
    }

    private void createExplanationVertex(
        GraphTraversalSource g,
        String questionId,
        Explanation explanation,
        long now,
        AtomicInteger explanationIdx,
        String questionType) {
        String explanationId = g.addV(VertexLabel.EXPLANATION.getString())
                                .property(ExplanationVertexProperty.TEXT.getString(), explanation.getText().getValue())
                                .property(DateProperty.CREATE_TIME.getString(), now)
                                .property(DateProperty.UPDATE_TIME.getString(), now)
                                .next()
                                .id()
                                .toString();

        g.V(explanationId)
         .hasLabel(VertexLabel.EXPLANATION.toString())
         .addE(EdgeLabel.INCLUDE.getString())
         .property(IncludeEdgeProperty.NUMBER.getString(), explanationIdx.get())
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(questionId).hasLabel(questionType))
         .iterate();


        AtomicInteger referenceIdx = new AtomicInteger();
        explanation.getReferenceIds().forEach(r -> {
            g.V(explanationId)
             .hasLabel(VertexLabel.EXPLANATION.toString())
             .addE(EdgeLabel.RELATED_TO.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), referenceIdx.get())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .to(g.V(r).hasLabel(VertexLabel.ARTICLE_BLOCK.toString()))
             .iterate();

            referenceIdx.getAndIncrement();
        });
    }

    private void addStatus(GraphTraversalSource g, String testId, String status, String authorId, long now) {
        g.V(testId)
         .hasLabel(VertexLabel.TEST.getString())
         .addE(status)
         .property(T.id, EdgeIdCreator.createId(authorId, testId, status))
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(authorId).hasLabel(VertexLabel.USER.getString()))
         .next();
    }
}
