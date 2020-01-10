package co.jp.wever.graphql.domain.domainmodel.test;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Questions {
    private List<Question> questions;
    private final static int MAX_QUESTION_COUNT = 20;

    private Questions(List<Question> questions) {
        this.questions = questions;
    }

    public static Questions of(List<Question> questions){
        if(questions.size() == 0){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_QUESTION.getString());
        }

        if(questions.size() > MAX_QUESTION_COUNT){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.QUESTION_OVER.getString());
        }

        return new Questions(questions);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
