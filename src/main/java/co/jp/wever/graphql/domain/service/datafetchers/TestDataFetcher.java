package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.test.TestInputConverter;
import co.jp.wever.graphql.application.datamodel.request.test.TestInput;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.test.TestMutationService;
import co.jp.wever.graphql.domain.service.test.TestQueryService;
import graphql.schema.DataFetcher;

@Component
public class TestDataFetcher {

    private final TestQueryService testQueryService;
    private final TestMutationService testMutationService;
    private final RequesterConverter requesterConverter;

    public TestDataFetcher(
        TestQueryService testQueryService,
        TestMutationService testMutationService,
        RequesterConverter requesterConverter) {
        this.testQueryService = testQueryService;
        this.testMutationService = testMutationService;
        this.requesterConverter = requesterConverter;
    }
    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////


    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher deleteTestDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String testId = dataFetchingEnvironment.getArgument("testId");
//            articleMutationService.delete(articleId, requester);

            return true;
        };
    }

    public DataFetcher publishTestDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            TestInput testInput = TestInputConverter.toTestInput(dataFetchingEnvironment);
            return CreateResponse.builder().id("").build();
        };
    }

    public DataFetcher draftTestDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            TestInput testInput = TestInputConverter.toTestInput(dataFetchingEnvironment);

            return CreateResponse.builder().id("").build();
        };
    }
}

