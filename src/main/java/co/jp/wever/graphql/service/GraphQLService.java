package co.jp.wever.graphql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

//import co.jp.wever.graphql.repository.PlanRepository;
import co.jp.wever.graphql.service.datafetcher.PlanDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {
//    @Autowired
//    PlanRepository planRepository;

    private GraphQL graphQL;

    @Autowired
    private PlanDataFetcher planDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException{
        File schemaFile = new ClassPathResource("schema.graphqls").getFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring(){
        return RuntimeWiring.newRuntimeWiring()
            .type("Query", typeWiring -> typeWiring
                .dataFetcher("plan", planDataFetcher))
            .build();
    }

    public GraphQL getGraphQL(){
        return graphQL;
    }
}
