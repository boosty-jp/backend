package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.CourseVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseBaseEntity;

public class CourseBaseEntityConverter {
    public static CourseBaseEntity toCourseBaseEntity(Map<Object, Object> courseVertex, String status) {
        return CourseBaseEntity.builder()
                               .id(VertexConverter.toId(courseVertex))
                               .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(), courseVertex))
                               .imageUrl(VertexConverter.toString(CourseVertexProperty.IMAGE_URL.getString(),
                                                                  courseVertex))
                               .description(VertexConverter.toString(CourseVertexProperty.DESCRIPTION.getString(),
                                                                     courseVertex))
                               .status(status)
                               .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), courseVertex))
                               .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), courseVertex))
                               .build();
    }
}
