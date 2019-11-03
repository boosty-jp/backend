package co.jp.wever.graphql.domain.domainmodel.plan.base.publish;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PublishPlanTitle {
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 100;
    private String value;

    private PublishPlanTitle(String value) {
        this.value = value;
    }

    public static PublishPlanTitle of(String title) {
        if (title.length() < MIN_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TITLE.getString());
        }

        if (title.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new PublishPlanTitle(title);
    }

    public String getValue() {
        return this.value;
    }
}