package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.plan.DeletePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;

@Component
public class DeletePlanRepositoryImpl implements DeletePlanRepository {
    private final NeptuneClient neptuneClient;

    public DeletePlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void deleteOne(String planId, String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();

        // 作者とのエッジを切る
        g.V(planId).inE(UserToPlanEdge.PUBLISH.name(), UserToPlanEdge.DRAFT.name()).from(g.V(userId)).drop();

        // 論理削除
        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToPlanEdge.DELETE.name())
         .to(g.V(planId))
         .property(UserToPlanProperty.DELETED.name(), now)
         .next();

        // TODO: Algoliaからデータを削除する
    }
}
