package co.jp.wever.graphql.application.datamodel.response.query.course;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.skill.SkillResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentResponse {
    private String id;
    private String title;
    private long number;
    private List<SkillResponse> skills;
    private boolean learned;
}
