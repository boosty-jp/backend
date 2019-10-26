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
        g.V(planId).inE(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DRAFTED.getString()).from(g.V(userId)).drop();

        // 論理削除
        long now = System.currentTimeMillis();
        g.V(userId)
         .addE(UserToPlanEdge.DELETED.getString())
         .to(g.V(planId))
         .property(UserToPlanProperty.DELETED_TIME.getString(), now)
         .next();

        // TODO: Algoliaからデータを削除する
    }
}
