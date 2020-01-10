package co.jp.wever.graphql.domain.domainmodel.test.explanation;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ReferenceId {
    private String value;

    private ReferenceId(String value) {
        this.value = value;
    }

    public static ReferenceId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.REFERENCE_ID_EMPTY.getString());
        }

        return new ReferenceId(value);
    }

    public String getValue() {
        return value;
    }
}
