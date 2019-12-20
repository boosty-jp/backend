package co.jp.wever.graphql.infrastructure.repository.course;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.domain.repository.course.CourseQueryRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.course.CourseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.valueMap;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;

@Component
public class CourseQueryRepositoryImpl implements CourseQueryRepository {

    private final NeptuneClient neptuneClient;

    public CourseQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public CourseEntity findOne(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(courseId)
                                         .hasLabel(VertexLabel.COURSE.getString())
                                         .project("base",
                                                  "sections",
                                                  "tags",
                                                  "author",
                                                  "status",
                                                  "userLiked",
                                                  "userLearned",
                                                  "liked",
                                                  "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.COURSE_SECTION.getString())
                                               .project("sectionBase", "sectionNumber", "sectionContents")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                     .values(IncludeEdgeProperty.NUMBER.getString()))
                                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                                     .hasLabel(VertexLabel.ARTICLE.getString())
                                                     .project("contentBase", "contentNumber", "contentLearned")
                                                     .by(__.valueMap().with(WithOptions.tokens))
                                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                                     .by(coalesce(__.inE(EdgeLabel.LEARN.getString())
                                                                    .where(outV().hasId(userId)
                                                                                 .hasLabel(VertexLabel.USER.getString()))
                                                                    .limit(1)
                                                                    .constant(true), constant(false)))
                                                     .fold())
                                               .fold())
                                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                                   EdgeLabel.DELETE.getString(),
                                                   EdgeLabel.PUBLISH.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .project("base", "tags")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(EdgeLabel.RELATED_TO.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold()))
                                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                                    EdgeLabel.DELETE.getString(),
                                                    EdgeLabel.PUBLISH.getString()).label())
                                         .by(coalesce(__.inE(EdgeLabel.LIKE.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(coalesce(__.inE(EdgeLabel.LEARN.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(__.in(EdgeLabel.LIKE.getString()).count())
                                         .by(__.in(EdgeLabel.LEARN.getString()).count())
                                         .next();

        return CourseEntityConverter.toCourseEntity(allResult);
    }

    @Override
    public CourseEntity findOneForGuest(String courseId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(courseId)
                                         .hasLabel(VertexLabel.COURSE.getString())
                                         .project("base",
                                                  "sections",
                                                  "tags",
                                                  "author",
                                                  "status",
                                                  "liked",
                                                  "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.COURSE_SECTION.getString())
                                               .project("sectionBase", "sectionNumber", "sectionContents")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                     .values(IncludeEdgeProperty.NUMBER.getString()))
                                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                                     .hasLabel(VertexLabel.ARTICLE.getString())
                                                     .project("contentBase", "contentNumber")
                                                     .by(__.valueMap().with(WithOptions.tokens))
                                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                                     .fold())
                                               .fold())
                                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                                   EdgeLabel.DELETE.getString(),
                                                   EdgeLabel.PUBLISH.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .project("base", "tags")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(EdgeLabel.RELATED_TO.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold()))
                                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                                    EdgeLabel.DELETE.getString(),
                                                    EdgeLabel.PUBLISH.getString()).label())
                                         .by(__.in(EdgeLabel.LIKE.getString()).count())
                                         .by(__.in(EdgeLabel.LEARN.getString()).count())
                                         .next();

        return CourseEntityConverter.toCourseEntityForGuest(allResult);
    }

    @Override
    public String findAuthorId(String courseId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return (String) g.V(courseId)
                         .hasLabel(VertexLabel.COURSE.getString())
                         .in(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString())
                         .id()
                         .next();
    }

    @Override
    public List<Long> findLearnList(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Long> learnList = g.V(courseId)
                                .hasLabel(VertexLabel.COURSE.getString())
                                .out(EdgeLabel.INCLUDE.getString())
                                .hasLabel(VertexLabel.COURSE_SECTION.getString())
                                .out(EdgeLabel.INCLUDE.getString())
                                .hasLabel(VertexLabel.ARTICLE.getString())
                                .inE(EdgeLabel.LEARN.getString())
                                .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                .count()
                                .toList();
    }
}
