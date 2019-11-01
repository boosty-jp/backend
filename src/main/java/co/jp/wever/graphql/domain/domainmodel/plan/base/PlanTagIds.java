package co.jp.wever.graphql.domain.domainmodel.plan.base;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PlanTagIds {
    private List<String> value;
    private final static int MAX_TAG_COUNT = 5;

    private PlanTagIds(List<String> value) {
        this.value = value;
    }

    public static PlanTagIds of(List<String> value) {
        if (value.size() > MAX_TAG_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        return new PlanTagIds(value);
    }

    public List<String> getValue() {
        return value;
    }
}
