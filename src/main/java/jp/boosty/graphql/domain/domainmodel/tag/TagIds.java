package jp.boosty.graphql.domain.domainmodel.tag;

import org.springframework.http.HttpStatus;

import java.util.List;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class TagIds {
    private List<String> value;
    private final static int MAX_TAG_COUNT = 5;

    private TagIds(List<String> value) {
        this.value = value;
    }

    public static TagIds of(List<String> value) {
        if (value.size() > MAX_TAG_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        return new TagIds(value);
    }

    public List<String> getValue() {
        return value;
    }
}
