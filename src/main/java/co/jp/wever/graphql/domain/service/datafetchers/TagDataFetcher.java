package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class TagDataFetcher {
    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher followTagDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
