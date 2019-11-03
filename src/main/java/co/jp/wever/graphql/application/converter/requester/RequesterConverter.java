package co.jp.wever.graphql.application.converter.requester;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import graphql.schema.DataFetchingEnvironment;
import io.netty.util.internal.StringUtil;

@Component
public class RequesterConverter {
    private final TokenVerifier tokenVerifier;

    public RequesterConverter(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    public Requester toRequester(DataFetchingEnvironment dataFetchingEnvironment) {
        String token = (String) dataFetchingEnvironment.getContext();
        boolean guest = false;
        String userId = "";

        if (StringUtil.isNullOrEmpty(token)) {
            guest = true;
        } else {
            userId = tokenVerifier.getUserId(token);
        }

        return Requester.builder().userId(userId).guest(guest).build();
    }
}
