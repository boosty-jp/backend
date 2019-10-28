package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PlanElementTitle {
    private String value;

    private PlanElementTitle(String value) {
        this.value = value;
    }

    public static PlanElementTitle of(String value) {
        if (value.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TITLE.getString());
        }

        return new PlanElementTitle(value);
    }

    public String getValue(){
        return value;
    }
}
