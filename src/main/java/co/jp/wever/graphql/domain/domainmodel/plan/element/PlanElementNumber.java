package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PlanElementNumber {
    private final static int MIN_NUMBER = 1;
    private final static int MAX_NUMBER = 30;

    private int value;

    private PlanElementNumber(int value) {
        this.value = value;
    }

    public static PlanElementNumber of(int value) {
        if (value < MIN_NUMBER) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_EMPTY.getString());
        }

        if (value > MAX_NUMBER) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_OVER.getString());
        }

        return new PlanElementNumber(value);
    }

    public int getValue() {
        return value;
    }
}
