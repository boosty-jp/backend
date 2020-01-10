package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.QuestionType;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TextQuestionVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.QuestionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.test.TextAnswerEntity;

public class TextQuestionConverter {
    public static QuestionEntity toTextAnswerEntities(Map<Object, Object> vertex) {
        Map<Object, Object> textQuestionVertex = (Map<Object, Object>) vertex.get("textQuestion");
        return QuestionEntity.builder()
                             .id(VertexConverter.toId(textQuestionVertex))
                             .text(VertexConverter.toString(TextQuestionVertexProperty.QUESTION_TEXT.getString(),
                                                            textQuestionVertex))
                             .type(QuestionType.TEXT.getString())
                             .number((int) vertex.get("number"))
                             .textAnswer(TextAnswerEntity.builder()
                                                         .answer(VertexConverter.toString(TextQuestionVertexProperty.ANSWER
                                                                                              .getString(),
                                                                                          textQuestionVertex))
                                                         .showCount(VertexConverter.toBool(TextQuestionVertexProperty.SHOW_COUNT
                                                                                               .getString(),
                                                                                           textQuestionVertex))
                                                         .build())
                             .build();
    }
}
