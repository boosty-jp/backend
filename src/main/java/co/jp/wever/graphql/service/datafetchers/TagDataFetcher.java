package co.jp.wever.graphql.service.datafetchers;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.datamodel.ErrorResponse;
import co.jp.wever.graphql.datamodel.UpdateResponse;
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
