package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.jp.wever.graphql.infrastructure.datamodel.test.QuestionEntity;

public class QuestionsEntityConverter {
    public static List<QuestionEntity> toQuestionsEntity(Map<Object, Object> questionVertex) {
        List<Map<Object, Object>> textQuestions = (List<Map<Object, Object>>) questionVertex.get("textQuestions");
        List<Map<Object, Object>> selectQuestions = (List<Map<Object, Object>>) questionVertex.get("selectQuestions");

        List<QuestionEntity> textQuestionEntities = textQuestions.stream()
                                                                 .map(tq -> TextQuestionConverter.toTextAnswerEntities(
                                                                     tq))
                                                                 .collect(Collectors.toList());

        List<QuestionEntity> selectQuestionEntities = selectQuestions.stream()
                                                                   .map(sq -> SelectQuestionConverter.toSelectAnswerEntities(
                                                                       sq))
                                                                   .collect(Collectors.toList());

        return Stream.of(textQuestionEntities, selectQuestionEntities)
                     .flatMap(list -> list.stream())
                     .sorted(Comparator.comparingInt(QuestionEntity::getNumber)).collect(Collectors.toList());
    }
}
