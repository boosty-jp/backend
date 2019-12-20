package co.jp.wever.graphql.infrastructure.datamodel.course;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.AccountActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.ActionCountEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseEntity {
    private CourseBaseEntity base;
    private List<TagEntity> tags;
    private List<CourseSectionEntity> sections;
    private UserEntity author;

    private ActionCountEntity actionCounts;
    private AccountActionEntity accountActions;
    private CourseLearnStatusEntity learnStatus;
}