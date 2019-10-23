package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.DeleteSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.user;

@Component
public class DeleteSectionRepositoryImpl implements DeleteSectionRepository {
    private final NeptuneClient neptuneClient;

    public DeleteSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void deleteOne(
        String articleId, String sectionId, String userId, List<SectionNumberEntity> decrementNumbers) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis() / 1000L;

        g.E(userId + "-" + sectionId).hasLabel(UserToSectionEdge.CREATED.getString()).drop().iterate();
        g.V(userId)
         .addE(UserToSectionEdge.DELETED.getString())
         .property(UserToSectionProperty.DELETED_TIME.getString(), now)
         .to(g.V(sectionId))
         .iterate();

        // 削除するセクションより後のものは番号をでデクリメントする
        decrementNumbers.stream().forEach(s -> {
            g.E(articleId + "-" + s.getId())
             .property(ArticleToSectionProperty.NUMBER.getString(), s.getNumber())
             .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
             .iterate();
        });

        // TODO: セクションが作者以外からのリファレンスをうけるようになるときは、修正する
        g.V(sectionId).inE(ArticleToSectionEdge.INCLUDE.getString()).where(outV().hasId(articleId)).drop().iterate();
    }
}
