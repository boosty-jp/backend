package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.CourseVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;

public class CourseSectionEntityConverter {
    public static CourseSectionEntity toCourseSectionEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("sectionBase");
        List<Map<String, Object>> sectionContentResults = (List<Map<String, Object>>) result.get("sectionContents");
        return CourseSectionEntity.builder()
                                  .id(VertexConverter.toId(baseResult))
                                  .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(), baseResult))
                                  .number((long) result.get("sectionNumber"))
                                  .courseSectionContents(sectionContentResults.stream()
                                                                              .map(s -> CourseSectionContentEntityConverter
                                                                                  .toCourseSectionContentEntity(s))
                                                                              .collect(Collectors.toList()))
                                  .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), baseResult))
                                  .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), baseResult))
                                  .build();
    }

    public static CourseSectionEntity toCourseSectionEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("sectionBase");
        List<Map<String, Object>> sectionContentResults = (List<Map<String, Object>>) result.get("sectionContents");
        return CourseSectionEntity.builder()
                                  .id(VertexConverter.toId(baseResult))
                                  .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(), baseResult))
                                  .number((long) result.get("sectionNumber"))
                                  .courseSectionContents(sectionContentResults.stream()
                                                                              .map(s -> CourseSectionContentEntityConverter
                                                                                  .toCourseSectionContentEntityForGuest(s))
                                                                              .collect(Collectors.toList()))
                                  .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), baseResult))
                                  .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), baseResult))
                                  .build();
    }

}
