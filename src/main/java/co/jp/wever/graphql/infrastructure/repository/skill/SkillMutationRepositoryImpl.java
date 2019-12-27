package co.jp.wever.graphql.infrastructure.repository.skill;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.domainmodel.skill.SkillName;
import co.jp.wever.graphql.domain.repository.skill.SkillMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SkillVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SkillSearchEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.unfold;

@Component
public class SkillMutationRepositoryImpl implements SkillMutationRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public SkillMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public String createSkill(SkillName name) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String skillId = g.V()
                          .hasLabel(VertexLabel.SKILL.getString())
                          .has(SkillVertexProperty.NAME.getString(), name.getValue())
                          .fold()
                          .coalesce(unfold(),
                                    g.addV(VertexLabel.SKILL.getString())
                                     .property(SkillVertexProperty.NAME.getString(), name.getValue())
                                     .property(DateProperty.CREATE_TIME.getString(), now)
                                     .property(DateProperty.UPDATE_TIME.getString(), now))
                          .next()
                          .id()
                          .toString();

        algoliaClient.getSkillIndex()
                     .saveObjectAsync(SkillSearchEntity.builder().objectID(skillId).name(name.getValue()).relatedCount(0).build());

        return skillId;
    }
}
