package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.response.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class UserDataFetcher {

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher followUserDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher unFollowUserDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
