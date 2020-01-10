package co.jp.wever.graphql.domain.domainmodel.test.answer;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SelectAnswers {
    private List<SelectAnswer> selectAnswers;
    private final static int MIN_ANSWER_SIZE = 2;
    private final static int MAX_ANSWER_SIZE = 5;

    public SelectAnswers(List<SelectAnswer> selectAnswers) {
        this.selectAnswers = selectAnswers;
    }

    public static SelectAnswers of(List<SelectAnswer> selectAnswers) {
        if (Objects.isNull(selectAnswers)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.SELECT_ANSWER_SHORTAGE.getString());
        }

        if (selectAnswers.size() < MIN_ANSWER_SIZE) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.SELECT_ANSWER_SHORTAGE.getString());
        }

        if (selectAnswers.size() > MAX_ANSWER_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SELECT_ANSWER_OVER.getString());
        }

        if (selectAnswers.stream().filter(SelectAnswer::isAnswer).count() != 1) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SELECT_ANSWERS.getString());
        }

        return new SelectAnswers(selectAnswers);
    }

    public List<SelectAnswer> getSelectAnswers() {
        return selectAnswers;
    }

}
