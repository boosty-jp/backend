package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.AccountActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.ActionCountEntity;

public class CourseEntityConverter {
    public static CourseEntity toCourseEntity(Map<String, Object> allResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) allResult.get("base");
        List<Map<String, Object>> sectionResult = (List<Map<String, Object>>) allResult.get("sections");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) allResult.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) allResult.get("author");
        String statusResult = allResult.get("status").toString();

        boolean userLiked = (boolean) allResult.get("userLiked");
        boolean userLearned = (boolean) allResult.get("userLearned");
        long like = (long) allResult.get("liked");
        long learned = (long) allResult.get("learned");

        return CourseEntity.builder()
                           .base(CourseBaseEntityConverter.toCourseBaseEntity(baseResult, statusResult))
                           .sections(sectionResult.stream()
                                                  .map(s -> CourseSectionEntityConverter.toCourseSectionEntity(s))
                                                  .collect(Collectors.toList()))
                           .tags(tagResult.stream()
                                          .map(t -> TagEntityConverter.toTagEntity(t))
                                          .collect(Collectors.toList()))
                           .author(UserEntityConverter.toUserEntity(authorResult))
                           .actionCounts(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                           .accountActions(AccountActionEntity.builder().learned(userLearned).liked(userLiked).build())
                           .build();
    }

    public static CourseEntity toCourseEntityForGuest(Map<String, Object> allResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) allResult.get("base");
        List<Map<String, Object>> sectionResult = (List<Map<String, Object>>) allResult.get("sections");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) allResult.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) allResult.get("author");
        String statusResult = allResult.get("status").toString();

        long like = (long) allResult.get("liked");
        long learned = (long) allResult.get("learned");

        return CourseEntity.builder()
                           .base(CourseBaseEntityConverter.toCourseBaseEntity(baseResult, statusResult))
                           .sections(sectionResult.stream()
                                                  .map(s -> CourseSectionEntityConverter.toCourseSectionEntityForGuest(s))
                                                  .collect(Collectors.toList()))
                           .tags(tagResult.stream()
                                          .map(t -> TagEntityConverter.toTagEntity(t))
                                          .collect(Collectors.toList()))
                           .author(UserEntityConverter.toUserEntity(authorResult))
                           .actionCounts(ActionCountEntity.builder().likeCount(like).learnedCount(learned).build())
                           .accountActions(AccountActionEntity.builder().learned(false).liked(false).build())
                           .build();
    }
}
