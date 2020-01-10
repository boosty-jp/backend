package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TestVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.TestBaseEntity;

public class TestBaseEntityConverter {
    public static TestBaseEntity toTestBaseEntity(
        Map<Object, Object> testVertex, String status) {
        return TestBaseEntity.builder()
                             .id(VertexConverter.toId(testVertex))
                             .title(VertexConverter.toString(TestVertexProperty.TITLE.getString(), testVertex))
                             .description(VertexConverter.toString(TestVertexProperty.DESCRIPTION.getString(),
                                                                   testVertex))
                             .status(status)
                             .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), testVertex))
                             .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), testVertex))
                             .build();
    }
}
