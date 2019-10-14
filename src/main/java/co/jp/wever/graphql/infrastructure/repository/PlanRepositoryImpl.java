package co.jp.wever.graphql.infrastructure.repository;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.PlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.datamodel.Plan;

@Component
public class PlanRepositoryImpl implements PlanRepository {

    private final NeptuneClient neptuneClient;

    public PlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public Plan findOne(String id) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        System.out.println("== find one ==");

        GraphTraversal t = g.V().limit(3).valueMap();

        t.forEachRemaining(
            e ->  System.out.println(e)
        );

        return Plan.builder().id(1).name("hoge").price(100).deleted(false).publish(false).description("des").image("iamge").build();
    }

    @Override
    public Plan findAll(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllPublished(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllDrafted(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLiked(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLearning(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLearned(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findFamous(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findRelated(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan initOne(String id) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.addV("Custom Label").property(T.id, id).property("name", "Custom id vertex 1").next();
        // ユーザーデータとの紐付け
        // Add a vertex with a user-supplied ID.
        g.addV("Custom Label").property(T.id, id).property("name", "Custom id vertex 1").next();
        g.addV("Custom Label").property(T.id, id+ "hoge").property("name", "Custom id vertex 2").next();

        // This gets the vertices, only.
        GraphTraversal t = g.V().limit(10).valueMap();

        t.forEachRemaining(
            e ->  System.out.println(e)
        );

        return Plan.builder().build();
    }

    @Override
    public Plan addOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan updateOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan deleteOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan publishOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan draftOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan startOne(String id) {
        return Plan.builder().build();
    }
}
