package co.jp.wever.graphql.infrastructure.connector;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
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
        @Value("${aws.neptune.certificateFilePath}") String certificateFilePath,
        @Value("${aws.neptune.port}") int port,
        @Value("${aws.neptune.useSSL}") boolean useSSL,
        @Value("${aws.neptune.maxInProcessPerConnection}") int maxInProcessPerConnection,
        @Value("${aws.neptune.minInProcessPerConnection}") int minInProcessPerConnection,
        @Value("${aws.neptune.maxConnectionPoolSize}") int maxConnectionPoolSize,
        @Value("${aws.neptune.minConnectionPoolSize}") int minConnectionPoolSize,
        @Value("${aws.neptune.maxSimultaneousUsagePerConnection}") int maxSimultaneousUsagePerConnection,
        @Value("${aws.neptune.minSimultaneousUsagePerConnection}") int minSimultaneousUsagePerConnection) {
        init(endpoint,
             certificateFilePath,
             port,
             useSSL,
             maxInProcessPerConnection,
             minInProcessPerConnection,
             maxConnectionPoolSize,
             minConnectionPoolSize,
             maxSimultaneousUsagePerConnection,
             minSimultaneousUsagePerConnection);
    }

    private void init(
        String endpoint,
        String certificateFilePath,
        int port,
        boolean useSSL,
        int maxInProcessPerConnection,
        int minInProcessPerConnection,
        int maxConnectionPoolSize,
        int minConnectionPoolSize,
        int maxSimultaneousUsagePerConnection,
        int minSimultaneousUsagePerConnection) {
        try {
            cluster = Cluster.build()
                             .addContactPoint(endpoint)
                             .enableSsl(useSSL)
                             .keyCertChainFile(certificateFilePath)
                             .port(port)
                             .maxInProcessPerConnection(maxInProcessPerConnection)
                             .minInProcessPerConnection(minInProcessPerConnection)
                             .maxConnectionPoolSize(maxConnectionPoolSize)
                             .minConnectionPoolSize(minConnectionPoolSize)
                             .maxSimultaneousUsagePerConnection(maxSimultaneousUsagePerConnection)
                             .minSimultaneousUsagePerConnection(minSimultaneousUsagePerConnection)
                             .keepAliveInterval(6000000)
                             .reconnectInterval(3000)
                             .serializer(Serializers.GRAPHBINARY_V1D0)
                             .create();

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
