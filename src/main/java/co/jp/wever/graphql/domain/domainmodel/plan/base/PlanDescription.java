package co.jp.wever.graphql.domain.domainmodel.plan.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PlanDescription {
    private final static int MAX_WORD_COUNT = 500;
    private String value;

    private PlanDescription(String value) {
        this.value = value;
    }

    public static PlanDescription of(String description) {
        if (description.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(), GraphQLErrorMessage.TEXT_OVER.getString());
        }

        return new PlanDescription(description);
    }

    public String getValue() {
        return this.value;
    }
}
