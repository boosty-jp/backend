package co.jp.wever.graphql.application.converter.course;

import java.util.Date;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.AccountActionResponseConverter;
import co.jp.wever.graphql.application.converter.user.ActionCountResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.course.CourseResponse;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class CourseResponseConverter {
    public static CourseResponse toCourseResponse(CourseEntity courseEntity) {
        return CourseResponse.builder()
                             .id(courseEntity.getBase().getId())
                             .title(courseEntity.getBase().getTitle())
                             .description(courseEntity.getBase().getDescription())
                             .imageUrl(courseEntity.getBase().getImageUrl())
                             .status(courseEntity.getBase().getStatus())
                             .createDate(DateToStringConverter.toDateString(new Date(courseEntity.getBase()
                                                                                                 .getCreatedDate())))
                             .updateDate(DateToStringConverter.toDateString(new Date(courseEntity.getBase()
                                                                                                 .getUpdatedDate())))
                             .tags(courseEntity.getTags()
                                               .stream()
                                               .map(t -> TagResponseConverter.toTagResponse(t))
                                               .collect(Collectors.toList()))
                             .sections(courseEntity.getSections()
                                                   .stream()
                                                   .map(s -> SectionResponseConverter.toSectionResponse(s))
                                                   .collect(Collectors.toList()))
                             .author(UserResponseConverter.toUserResponse(courseEntity.getAuthor()))
                             .accountAction(AccountActionResponseConverter.toAccountAction(courseEntity.getAccountActions()))
                             .actionCount(ActionCountResponseConverter.toActionCountResponse(courseEntity.getActionCounts()))
                             .build();

    }
}
