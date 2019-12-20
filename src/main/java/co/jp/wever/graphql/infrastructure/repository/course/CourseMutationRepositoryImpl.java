package co.jp.wever.graphql.infrastructure.repository.course;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.course.Course;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSection;
import co.jp.wever.graphql.domain.domainmodel.tag.TagId;
import co.jp.wever.graphql.domain.repository.course.CourseMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.CourseVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SectionVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.course.CourseSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class CourseMutationRepositoryImpl implements CourseMutationRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public CourseMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void publish(String courseId, Course course, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        updateCourseVertex(g, courseId, course, now);

        clearTag(g, courseId);
        addTags(g, courseId, course, now);

        clearSections(g, courseId, now);
        addSections(g, courseId, course, now);

        clearStatus(g, courseId, userId);
        addStatus(g, courseId, userId, EdgeLabel.PUBLISH.getString(), now);

        algoliaClient.getCourseIndex()
                     .saveObjectAsync(CourseSearchEntityConverter.toCourseSearchEntity(courseId, course, userId, now));
    }

    @Override
    public String publishByEntry(Course course, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String courseId = createCourseVertex(g, course, now);

        addTags(g, courseId, course, now);
        addSections(g, courseId, course, now);
        addStatus(g, courseId, userId, EdgeLabel.PUBLISH.getString(), now);

        algoliaClient.getCourseIndex()
                     .saveObjectAsync(CourseSearchEntityConverter.toCourseSearchEntity(courseId, course, userId, now));

        return courseId;
    }

    @Override
    public void draft(String courseId, Course course, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        updateCourseVertex(g, courseId, course, now);

        clearTag(g, courseId);
        addTags(g, courseId, course, now);

        clearSections(g, courseId, now);
        addSections(g, courseId, course, now);

        clearStatus(g, courseId, userId);
        addStatus(g, courseId, userId, EdgeLabel.DRAFT.getString(), now);

        algoliaClient.getCourseIndex().deleteObjectAsync(courseId);
    }

    @Override
    public String draftByEntry(Course course, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String courseId = createCourseVertex(g, course, now);

        addTags(g, courseId, course, now);
        addSections(g, courseId, course, now);
        addStatus(g, courseId, userId, EdgeLabel.DRAFT.getString(), now);

        algoliaClient.getCourseIndex().deleteObjectAsync(courseId);

        return courseId;
    }

    @Override
    public void delete(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearStatus(g, courseId, userId);
        addStatus(g, courseId, EdgeLabel.DELETE.getString(), userId, now);

        algoliaClient.getArticleIndex().deleteObjectAsync(courseId);
    }

    @Override
    public void like(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        addAction(g, courseId, EdgeLabel.LIKE.getString(), userId, now);
    }

    @Override
    public void clearLike(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        clearAction(g, courseId, EdgeLabel.LIKE.getString(), userId);
    }

    @Override
    public void start(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        addAction(g, courseId, EdgeLabel.ONGOING.getString(), userId, now);
    }

    @Override
    public void clearStart(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearAction(g, courseId, EdgeLabel.ONGOING.getString(), userId);
    }

    @Override
    public void complete(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearAction(g, courseId, EdgeLabel.ONGOING.getString(), userId);
        addAction(g, courseId, EdgeLabel.LEARN.getString(), userId, now);
    }

    private String createCourseVertex(GraphTraversalSource g, Course course, long now) {
        return g.addV(VertexLabel.COURSE.getString())
                .property(CourseVertexProperty.TITLE.getString(), course.getBase().getTitle().getValue())
                .property(CourseVertexProperty.DESCRIPTION.getString(), course.getDescription().getValue())
                .property(CourseVertexProperty.IMAGE_URL.getString(), course.getBase().getImageUrl().getValue())
                .property(DateProperty.CREATE_TIME.getString(), now)
                .property(DateProperty.UPDATE_TIME.getString(), now)
                .next()
                .id()
                .toString();
    }

    private void updateCourseVertex(GraphTraversalSource g, String courseId, Course course, long now) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, CourseVertexProperty.TITLE.getString(), course.getBase().getTitle().getValue())
         .property(single, CourseVertexProperty.DESCRIPTION.getString(), course.getDescription().getValue())
         .property(single, CourseVertexProperty.IMAGE_URL.getString(), course.getBase().getImageUrl().getValue())
         .property(single, DateProperty.UPDATE_TIME.getString(), now)
         .next();
    }

    private void addTags(GraphTraversalSource g, String courseId, Course course, long now) {
        List<String> tagIds =
            course.getBase().getTagIds().getTagIds().stream().map(TagId::getValue).collect(Collectors.toList());

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(EdgeLabel.RELATED_TO.getString())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(courseId).hasLabel(VertexLabel.COURSE.getString()))
             .iterate();
        }
    }

    private void clearTag(GraphTraversalSource g, String courseId) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .outE(EdgeLabel.RELATED_TO.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();
    }

    private void clearSections(GraphTraversalSource g, String courseId, long now) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .out(EdgeLabel.INCLUDE.getString())
         .hasLabel(VertexLabel.COURSE_SECTION.getString())
         .addE(EdgeLabel.DELETE.getString())
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(courseId).hasLabel(VertexLabel.COURSE.getString()))
         .iterate();

        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .outE(EdgeLabel.INCLUDE.getString())
         .where(inV().hasLabel(VertexLabel.COURSE_SECTION.getString()))
         .drop()
         .iterate();
    }

    private void addSections(GraphTraversalSource g, String courseId, Course course, long now) {
        GraphTraversal t = g.V();
        List<CourseSection> sections = course.getSections().getSections();

        for (int i = 0; i < sections.size(); ++i) {
            String stepLabel = "s-" + i;
            CourseSection s = sections.get(i);

            t.addV(VertexLabel.COURSE_SECTION.getString())
             .property(SectionVertexProperty.TITLE.getString(), s.getTitle().getValue())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .as(stepLabel);

            t.hasId(courseId)
             .hasLabel(VertexLabel.COURSE.getString())
             .addE(EdgeLabel.INCLUDE.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), s.getNumber().getValue())
             .to(stepLabel);

            sections.get(i)
                    .getContents()
                    .getContents()
                    .forEach(c -> t.hasId(c.getId().getValue())
                                   .hasLabel(VertexLabel.ARTICLE.getString())
                                   .addE(EdgeLabel.INCLUDE.getString())
                                   .property(IncludeEdgeProperty.NUMBER.getString(), c.getNumber().getValue())
                                   .from(stepLabel));

            t.iterate();
        }
    }

    private void clearStatus(GraphTraversalSource g, String courseId, String authorId) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .inE(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString())
         .where(outV().hasLabel(VertexLabel.USER.getString()).hasId(authorId))
         .drop()
         .iterate();
    }

    private void addStatus(GraphTraversalSource g, String courseId, String status, String authorId, long now) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .addE(status)
         .property(T.id, EdgeIdCreator.createId(authorId, courseId, status))
         .from(g.V(authorId))
         .property(DateProperty.CREATE_TIME.getString(), now)
         .next();
    }

    private void addAction(GraphTraversalSource g, String courseId, String action, String userId, long now) {
        g.E(EdgeIdCreator.createId(userId, courseId, action))
         .fold()
         .coalesce(unfold(),
                   g.V(courseId)
                    .hasLabel(VertexLabel.COURSE.getString())
                    .addE(action)
                    .property(T.id, EdgeIdCreator.createId(userId, courseId, action))
                    .property(DateProperty.CREATE_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();
    }

    private void clearAction(GraphTraversalSource g, String courseId, String action, String userId) {
        g.V(courseId)
         .hasLabel(VertexLabel.COURSE.getString())
         .inE(action)
         .hasId(EdgeIdCreator.createId(userId, courseId, action))
         .drop()
         .iterate();
    }
}
