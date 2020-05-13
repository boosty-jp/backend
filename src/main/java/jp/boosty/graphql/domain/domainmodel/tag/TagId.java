package jp.boosty.graphql.domain.domainmodel.tag;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class TagId {

    private String value;

    private TagId(String value) {
        this.value = value;
    }

    public static TagId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_ID_EMPTY.getString());
        }

        return new TagId(value);
    }

    public String getValue() {
        return value;
    }
}
