package co.jp.wever.graphql.domain.domainmodel.test;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentText;
import co.jp.wever.graphql.domain.domainmodel.test.answer.SelectAnswers;
import co.jp.wever.graphql.domain.domainmodel.test.answer.TextAnswer;
import co.jp.wever.graphql.domain.domainmodel.test.explanation.Explanations;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Question {
    private ContentText questionText;
    private QuestionType type;
    private TextAnswer textAnswer;
    private SelectAnswers selectAnswers;
    private Explanations explanations;


    private Question(
        ContentText questionText,
        QuestionType type,
        TextAnswer textAnswer,
        SelectAnswers selectAnswers,
        Explanations explanations) {
        this.questionText = questionText;
        this.type = type;
        this.textAnswer = textAnswer;
        this.selectAnswers = selectAnswers;
        this.explanations = explanations;
    }

    public static Question of(
        ContentText questionText,
        QuestionType type,
        TextAnswer textAnswer,
        SelectAnswers selectAnswers,
        Explanations explanations) {
        if (Objects.isNull(questionText) || Objects.isNull(type) || Objects.isNull(explanations)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        if (type.isSelect()) {
            if (Objects.isNull(selectAnswers)) {
                throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                 GraphQLErrorMessage.NULL_DATA.getString());
            }
            return new Question(questionText, type, null, selectAnswers, explanations);
        } else if (type.isText()) {
            if (Objects.isNull(textAnswer)) {
                throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                 GraphQLErrorMessage.NULL_DATA.getString());
            }
            return new Question(questionText, type, textAnswer, null, explanations);
        }

        throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                         GraphQLErrorMessage.NULL_DATA.getString());
    }

    public ContentText getQuestionText() {
        return questionText;
    }

    public QuestionType getType() {
        return type;
    }

    public TextAnswer getTextAnswer() {
        return textAnswer;
    }

    public SelectAnswers getSelectAnswers() {
        return selectAnswers;
    }

    public Explanations getExplanations() {
        return explanations;
    }
}
