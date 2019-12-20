package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.CourseVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionContentEntity;

public class CourseSectionContentEntityConverter {
    public static CourseSectionContentEntity toCourseSectionContentEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("contentBase");
        long number = (long) result.get("contentNumber");
        boolean learned = (boolean) result.get("contentLearned");
        return CourseSectionContentEntity.builder()
                                         .id(VertexConverter.toId(baseResult))
                                         .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                         .number(number)
                                         .learned(learned)
                                         .build();
    }

    public static CourseSectionContentEntity toCourseSectionContentEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("contentBase");
        long number = (long) result.get("contentNumber");
        return CourseSectionContentEntity.builder()
                                         .id(VertexConverter.toId(baseResult))
                                         .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                         .number(number)
                                         .learned(false)
                                         .build();
    }
}
