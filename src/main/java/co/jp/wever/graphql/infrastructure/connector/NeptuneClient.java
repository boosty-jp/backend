package co.jp.wever.graphql.infrastructure.connector;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class NeptuneClient implements AutoCloseable {
    @Value("${aws.neptune.endpoint}")
    private String endpoint;

    @Value("${aws.neptune.port}")
    private int port;

    @Value("${aws.neptune.maxInProgressPerConnection}")
    private int maxInProcessPerConnection;

    @Value("${aws.neptune.minInProcessPerConnection}")
    private int minInProcessPerConnection;

    @Value("${aws.neptune.maxConnectionPoolSize}")
    private int maxConnectionPoolSize;

    @Value("${aws.neptune.minConnectionPoolSize}")
    private int minConnectionPoolSize;

    @Value("${aws.neptune.maxSimultaneousUsagePerConnection}")
    private int maxSimultaneousUsagePerConnection;

    @Value("${aws.neptune.minSimultaneousUsagePerConnection}")
    private int minSimultaneousUsagePerConnection;

    private Cluster cluster;
    private GraphTraversalSource g;

    NeptuneClient() {
        init();
    }

    private void init() {
        try {
            cluster = Cluster.build().addContactPoint(endpoint).port(port).maxInProcessPerConnection(maxInProcessPerConnection).minInProcessPerConnection(minInProcessPerConnection)
                .maxConnectionPoolSize(maxConnectionPoolSize).minConnectionPoolSize(minConnectionPoolSize).maxSimultaneousUsagePerConnection(maxSimultaneousUsagePerConnection)
                .minSimultaneousUsagePerConnection(minSimultaneousUsagePerConnection).create();

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
