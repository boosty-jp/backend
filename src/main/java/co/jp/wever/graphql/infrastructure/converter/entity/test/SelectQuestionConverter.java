package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.constant.QuestionType;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TextQuestionVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.QuestionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.test.SelectAnswerEntity;

public class SelectQuestionConverter {
    public static QuestionEntity toSelectAnswerEntities(Map<Object, Object> vertex) {
        Map<Object, Object> selectQuestionVertex = (Map<Object, Object>) vertex.get("selectQuestion");
        List<Map<Object, Object>> candidates = (List<Map<Object, Object>>) vertex.get("candidates");

        return QuestionEntity.builder()
                             .id(VertexConverter.toId(selectQuestionVertex))
                             .text(VertexConverter.toString(TextQuestionVertexProperty.QUESTION_TEXT.getString(),
                                                            selectQuestionVertex))
                             .type(QuestionType.SELECT.getString())
                             .number((int) vertex.get("number"))
                             .selectAnswers(candidates.stream()
                                                      .map(c -> SelectAnswerEntityConverter.toSelectAnswerEntity(c))
                                                      .sorted(Comparator.comparingInt(SelectAnswerEntity::getNumber))
                                                      .collect(Collectors.toList()))
                             .build();
    }
}
