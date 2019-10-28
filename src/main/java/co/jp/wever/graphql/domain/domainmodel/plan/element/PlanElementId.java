package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class PlanElementId {
    private String value;

    private PlanElementId(String value) {
        this.value = value;
    }

    public static PlanElementId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_ID_EMPTY.getString());
        }

        return new PlanElementId(value);
    }

    public String getValue() {
        return value;
    }
}
