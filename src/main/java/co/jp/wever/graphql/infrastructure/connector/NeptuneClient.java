package co.jp.wever.graphql.infrastructure.connector;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;


public class NeptuneClient implements AutoCloseable {
    private final String endpoint;

    private Cluster cluster;
    private GraphTraversalSource g;

    NeptuneClient(String endpoint) {
        this.endpoint = endpoint;
        init();
    }

    private void init() {
        try {
            cluster = Cluster.build().addContactPoint(endpoint).port(8182).maxInProcessPerConnection(1).minInProcessPerConnection(1).maxConnectionPoolSize(1).minConnectionPoolSize(1)
                .maxSimultaneousUsagePerConnection(1).minSimultaneousUsagePerConnection(1).create();

            g = AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster));

        } catch (Exception e) {
            //TODO: エラーログ出す
            if (cluster != null && !cluster.isClosed() && !cluster.isClosing()) {
                cluster.close();
            }
            throw new RuntimeException(e);
        }
    }

    GraphTraversalSource newTraversal() {
        return g;
    }

    @Override
    public void close() {
        if (cluster != null && !cluster.isClosed() && !cluster.isClosing()) {
            cluster.closeAsync();
        }
    }
}
