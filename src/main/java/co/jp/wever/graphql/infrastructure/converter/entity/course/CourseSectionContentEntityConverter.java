package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.CourseVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.skill.SkillEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionContentEntity;
import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;

public class CourseSectionContentEntityConverter {
    public static CourseSectionContentEntity toCourseSectionContentEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("contentBase");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) result.get("contentSkills");
        long number = (long) result.get("contentNumber");
        boolean learned = (boolean) result.get("contentLearned");

        List<SkillEntity> skills = skillResult.stream().map(s -> {
            Map<Object, Object> skillLevel = (Map<Object, Object>) s.get("contentSkillLevel");
            Map<Object, Object> skillBase = (Map<Object, Object>) s.get("contentSkillLevel");
            return SkillEntityConverter.toSkillEntity(skillLevel, skillBase);
        }).collect(Collectors.toList());

        return CourseSectionContentEntity.builder()
                                         .id(VertexConverter.toId(baseResult))
                                         .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                         .skills(skills)
                                         .number(number)
                                         .learned(learned)
                                         .build();
    }

    public static CourseSectionContentEntity toCourseSectionContentEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("contentBase");
        List<Map<String, Object>> skillResult = (List<Map<String, Object>>) result.get("contentSkills");
        long number = (long) result.get("contentNumber");

        List<SkillEntity> skills = skillResult.stream().map(s -> {
            Map<Object, Object> skillLevel = (Map<Object, Object>) s.get("contentSkillLevel");
            Map<Object, Object> skillBase = (Map<Object, Object>) s.get("contentSkillLevel");
            return SkillEntityConverter.toSkillEntity(skillLevel, skillBase);
        }).collect(Collectors.toList());

        return CourseSectionContentEntity.builder()
                                         .id(VertexConverter.toId(baseResult))
                                         .title(VertexConverter.toString(CourseVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                         .skills(skills)
                                         .number(number)
                                         .learned(false)
                                         .build();
    }
}
