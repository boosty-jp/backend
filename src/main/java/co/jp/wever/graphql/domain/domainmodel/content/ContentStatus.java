package co.jp.wever.graphql.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public enum ContentStatus {
    PUBLISHED("published"), DRAFTED("drafted"), ENTRY("entry"), DELETED("deleted");

    String value;

    ContentStatus(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    public static ContentStatus fromString(String value) {
        for (ContentStatus s : ContentStatus.values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }

        throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                         GraphQLErrorMessage.INVALID_STATUS.getString());
    }
}
