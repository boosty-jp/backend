package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.SelectQuestionAnswerVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.SelectAnswerEntity;

public class SelectAnswerEntityConverter {
    public static SelectAnswerEntity toSelectAnswerEntity(Map<Object, Object> vertex) {
        Map<Object, Object> selectAnswerVertex = (Map<Object, Object>) vertex.get("answer");

        return SelectAnswerEntity.builder()
                                 .isAnswer(VertexConverter.toBool(SelectQuestionAnswerVertexProperty.IS_ANSWER.getString(),
                                                                  selectAnswerVertex))
                                 .text(VertexConverter.toString(SelectQuestionAnswerVertexProperty.TEXT.getString(),
                                                                  selectAnswerVertex))
                                 .number((int) vertex.get("number")).build();
    }
}
