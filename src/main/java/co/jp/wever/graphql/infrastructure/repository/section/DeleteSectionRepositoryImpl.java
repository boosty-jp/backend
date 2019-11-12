package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.DeleteSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Component
public class DeleteSectionRepositoryImpl implements DeleteSectionRepository {
    private final NeptuneClient neptuneClient;

    public DeleteSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void deleteOne(
        String articleId, String sectionId, String userId, List<SectionNumberEntity> decrementNumbers) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.CREATED.getString())).drop().iterate();

        g.E(EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.DELETED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToSectionEdge.DELETED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.DELETED.getString()))
                    .property(UserToSectionProperty.CREATED_TIME.getString(), now)
                    .property(UserToSectionProperty.UPDATED_TIME.getString(), now)
                    .to(g.V(sectionId)))
         .iterate();

        decrementNumbers.stream().forEach(s -> {
            g.E(EdgeIdCreator.createId(articleId, s.getId(), ArticleToSectionEdge.INCLUDE.getString()))
             .property(ArticleToSectionProperty.NUMBER.getString(), s.getNumber())
             .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
             .iterate();
        });

        // TODO: セクションが作者以外からのリファレンスをうけるようになるときは、修正する
        g.E(EdgeIdCreator.createId(articleId, sectionId, ArticleToSectionEdge.INCLUDE.getString())).drop().iterate();
    }
}
