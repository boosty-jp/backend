package co.jp.wever.graphql.infrastructure.repository.skill;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.skill.SkillEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Component
public class SkillQueryRepositoryImpl {
    private final NeptuneClient neptuneClient;

    public SkillQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public List<SkillEntity> famousSkill() {
        GraphTraversalSource g = neptuneClient.newTraversal();

        // スキルが付いている記事を学習したユーザーの数の上位10件
        List<Map<String, Object>> results = g.V()
                                             .hasLabel(VertexLabel.SKILL.getString())
                                             .project("skill", "learnedCount")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.in(EdgeLabel.TEACH.getString())
                                                   .hasLabel(VertexLabel.ARTICLE.getString())
                                                   .in(EdgeLabel.LEARN.getString())
                                                   .hasLabel(VertexLabel.USER.getString())
                                                   .count())
                                             .order()
                                             .by(select("learnedCount"), Order.desc)
                                             .limit(10)
                                             .toList();

        return results.stream()
                      .map(r -> SkillEntityConverter.toSkillEntity((Map<Object, Object>) r.get("skill")))
                      .collect(Collectors.toList());
    }
}
