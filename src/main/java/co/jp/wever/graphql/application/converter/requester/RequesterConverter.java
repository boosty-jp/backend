package co.jp.wever.graphql.application.converter.requester;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.infrastructure.client.TokenVerifier;
import graphql.schema.DataFetchingEnvironment;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

        log.info("userId: {}, guest: {}", userId, guest);
        return Requester.builder().userId(userId).guest(guest).build();
    }
}
