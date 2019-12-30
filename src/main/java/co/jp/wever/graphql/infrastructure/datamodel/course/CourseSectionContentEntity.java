package co.jp.wever.graphql.infrastructure.datamodel.course;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseSectionContentEntity {
    private String id;
    private int number;
    private String title;
    private List<SkillEntity> skills;
    private boolean learned;
}
