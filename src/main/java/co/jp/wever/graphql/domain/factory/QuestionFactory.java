package co.jp.wever.graphql.domain.factory;

import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.test.QuestionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentText;
import co.jp.wever.graphql.domain.domainmodel.test.Question;
import co.jp.wever.graphql.domain.domainmodel.test.QuestionType;
import co.jp.wever.graphql.domain.domainmodel.test.answer.SelectAnswer;
import co.jp.wever.graphql.domain.domainmodel.test.answer.SelectAnswers;
import co.jp.wever.graphql.domain.domainmodel.test.answer.Text;
import co.jp.wever.graphql.domain.domainmodel.test.answer.TextAnswer;
import co.jp.wever.graphql.domain.domainmodel.test.explanation.Explanation;
import co.jp.wever.graphql.domain.domainmodel.test.explanation.Explanations;
import co.jp.wever.graphql.domain.domainmodel.test.explanation.ReferenceIds;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class QuestionFactory {
    public static Question make(QuestionInput input) {
        ContentText questionText = ContentText.of(input.getQuestionText());
        QuestionType type = QuestionType.of(input.getType());
        Explanations explanations = Explanations.of(input.getExplanations()
                                                         .stream()
                                                         .map(e -> Explanation.of(ContentText.of(e.getText()),
                                                                                  ReferenceIds.of(e.getReferenceIds())))
                                                         .collect(Collectors.toList()));

        if (type.isSelect()) {
            SelectAnswers selectAnswers = SelectAnswers.of(input.getSelectAnswers()
                                                                .stream()
                                                                .map(s -> SelectAnswer.of(Text.of(s.getText()),
                                                                                          s.isAnswer()))
                                                                .collect(Collectors.toList()));
            return Question.of(questionText, type, null, selectAnswers, explanations);
        } else if (type.isText()) {
            TextAnswer textAnswer =
                TextAnswer.of(Text.of(input.getTextAnswer().getAnswer()), input.getTextAnswer().isShowCount());
            return Question.of(questionText, type, textAnswer, null, explanations);
        }


        throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                         GraphQLErrorMessage.NULL_DATA.getString());
    }
}
