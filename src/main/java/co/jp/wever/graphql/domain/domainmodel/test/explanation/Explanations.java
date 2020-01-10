package co.jp.wever.graphql.domain.domainmodel.test.explanation;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Explanations {
    private List<Explanation> explanations;
    private final static int MAX_EXPLANATION_SIZE = 5;

    public Explanations(List<Explanation> explanations) {
        this.explanations = explanations;
    }

    public static Explanations of(List<Explanation> explantions) {
        if (Objects.isNull(explantions)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.EMPTY_EXPLANATION.getString());
        }

        if (explantions.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.EMPTY_EXPLANATION.getString());
        }

        if (explantions.size() > MAX_EXPLANATION_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EXPLANATION_OVER.getString());
        }

        return new Explanations(explantions);
    }

    public List<Explanation> getExplanations() {
        return explanations;
    }
}
