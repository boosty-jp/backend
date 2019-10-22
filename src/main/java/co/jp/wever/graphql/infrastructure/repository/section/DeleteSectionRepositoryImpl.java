package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.DeleteSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.union;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

@Component
public class DeleteSectionRepositoryImpl implements DeleteSectionRepository {
    private final NeptuneClient neptuneClient;

    public DeleteSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void deleteOne(String sectionId, String userId, List<String> decrementIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis() / 1000L;

        g.V(sectionId).outE(UserToSectionEdge.CREATED.getString()).to(g.V(userId)).drop();
        g.V(userId)
         .addE(UserToSectionEdge.DELETED.getString())
         .property(UserToSectionProperty.DELETED_TIME.getString(), now)
         .to(g.V(userId));

        // 削除するセクションより後のものは番号をでデクリメントする
        g.V(decrementIds)
         .inE(ArticleToSectionEdge.INCLUDE.getString())
         .property(ArticleToSectionProperty.NUMBER.getString(),
                   union(values(ArticleToSectionProperty.NUMBER.getString()), __.constant(-1).sum()));

    }
}
