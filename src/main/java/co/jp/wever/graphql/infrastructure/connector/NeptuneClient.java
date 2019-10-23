package co.jp.wever.graphql.infrastructure.connector;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class NeptuneClient implements AutoCloseable {

    private Cluster cluster;
    private GraphTraversalSource g;

    public NeptuneClient(
        @Value("${aws.neptune.endpoint}") String endpoint,
        @Value("${aws.neptune.port}") int port,
        @Value("${aws.neptune.maxInProcessPerConnection}") int maxInProcessPerConnection,
        @Value("${aws.neptune.minInProcessPerConnection}") int minInProcessPerConnection,
        @Value("${aws.neptune.maxConnectionPoolSize}") int maxConnectionPoolSize,
        @Value("${aws.neptune.minConnectionPoolSize}") int minConnectionPoolSize,
        @Value("${aws.neptune.maxSimultaneousUsagePerConnection}") int maxSimultaneousUsagePerConnection,
        @Value("${aws.neptune.minSimultaneousUsagePerConnection}") int minSimultaneousUsagePerConnection) {
        init(endpoint,
             port,
             maxInProcessPerConnection,
             minInProcessPerConnection,
             maxConnectionPoolSize,
             minConnectionPoolSize,
             maxSimultaneousUsagePerConnection,
             minSimultaneousUsagePerConnection);
    }

    private void init(
        String endpoint,
        int port,
        int maxInProcessPerConnection,
        int minInProcessPerConnection,
        int maxConnectionPoolSize,
        int minConnectionPoolSize,
        int maxSimultaneousUsagePerConnection,
        int minSimultaneousUsagePerConnection) {
        try {
            cluster = Cluster.build()
                             .addContactPoint(endpoint)
                             .port(port)
                             .maxInProcessPerConnection(maxInProcessPerConnection)
                             .minInProcessPerConnection(minInProcessPerConnection)
                             .maxConnectionPoolSize(maxConnectionPoolSize)
                             .minConnectionPoolSize(minConnectionPoolSize)
                             .maxSimultaneousUsagePerConnection(maxSimultaneousUsagePerConnection)
                             .minSimultaneousUsagePerConnection(minSimultaneousUsagePerConnection)
                             .create();


            //TODO: Neptuneへの接続パラメータのチューニング行う
            //            cluster = Cluster.build().addContactPoint(endpoint).port(port).create();
            g = AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster));

        } catch (Exception e) {
            //TODO: エラーログ出す
            if (cluster != null && !cluster.isClosed() && !cluster.isClosing()) {
                cluster.close();
            }
            throw new RuntimeException(e);
        }
    }

    public GraphTraversalSource newTraversal() {
        return g;
    }

    @Override
    public void close() {
        if (cluster != null && !cluster.isClosed() && !cluster.isClosing()) {
            cluster.closeAsync();
        }
    }
}
